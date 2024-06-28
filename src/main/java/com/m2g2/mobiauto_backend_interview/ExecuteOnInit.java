package com.m2g2.mobiauto_backend_interview;

import com.m2g2.mobiauto_backend_interview.enums.DescricaoPapel;
import com.m2g2.mobiauto_backend_interview.model.Papel;
import com.m2g2.mobiauto_backend_interview.model.Revenda;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.repository.PapelRepository;
import com.m2g2.mobiauto_backend_interview.repository.RevendaRepository;
import com.m2g2.mobiauto_backend_interview.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/*
 * Esta classe é usada para popular os papéis dos usuários no banco de dados
 * sempre que a aplicação é iniciada. Embora funcione, esta não é a abordagem ideal
 * para gerenciar mudanças no banco de dados.
 *
 * Em vez disso, recomenda-se o uso de uma ferramenta de migração de banco de dados
 * como o Flyway. O Flyway permite versionar e gerenciar scripts de migração,
 * garantindo consistência e facilitando a manutenção do banco de dados ao longo do tempo.
 */

@Component
public class ExecuteOnInit implements ApplicationRunner {

    private final PapelRepository papelRepository;

    private final UsuarioRepository usuarioRepository;

    private final RevendaRepository revendaRepository;

    private final PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteOnInit.class);

    public ExecuteOnInit(PapelRepository papelRepository, UsuarioRepository usuarioRepository, RevendaRepository revendaRepository, PasswordEncoder passwordEncoder) {
        this.papelRepository = papelRepository;
        this.usuarioRepository = usuarioRepository;
        this.revendaRepository = revendaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOGGER.info("Iniciando população da base de dados...");
        // Popula a base de dados com os papéis dos usuários
        Papel admin = new Papel();
        admin.setDescricao(DescricaoPapel.ADMIN.name());

        Papel assistente = new Papel();
        assistente.setDescricao(DescricaoPapel.ASSISTENTE.name());

        Papel gerente = new Papel();
        gerente.setDescricao(DescricaoPapel.GERENTE.name());

        Papel proprietario = new Papel();
        proprietario.setDescricao(DescricaoPapel.PROPRIETARIO.name());

        List<Papel> papeis = papelRepository.saveAll(List.of(admin, assistente, gerente, proprietario));

        // Popula a base de dados com um usuário administrador
        Usuario adminUser = new Usuario();
        adminUser.setEmail("admin@admin.com");
        adminUser.setSenha(passwordEncoder.encode("admin"));
        adminUser.setPrimeiroNome("João");
        adminUser.setSobrenome("Silva");
        adminUser.setPapeis(Collections.singleton(papeis.get(0)));
        if (usuarioRepository.findByEmail(adminUser.getEmail()).isEmpty()) {
            usuarioRepository.save(adminUser);
        }

        // Popula a base de dados com uma revenda
        Revenda revenda = new Revenda();
        revenda.setCnpj("31621417000103");
        revenda.setRazaoSocial("Revenda Teste Ltda");
        revenda.setNomeFantasia("Revenda Teste");
        revendaRepository.save(revenda);
        LOGGER.info("População da base de dados finalizada com sucesso.");
    }
}
