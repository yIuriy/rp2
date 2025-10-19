import model.PapelUsuario;
import model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.JsonDataManager;
import service.UsuarioService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class UsuarioServiceTest {

    private JsonDataManager dataManager;
    private UsuarioService usuarioService;

    // Configuração executada antes de cada teste
    @BeforeEach
    void setUp() {
        // Garante que o estado inicial (dados simulados) seja redefinido para cada teste
        this.dataManager = new JsonDataManager();
        this.usuarioService = new UsuarioService(dataManager);
    }

    // REQUISITO: Visualizar todos os usuários cadastrados
    @Test
    void visualizarTodosDeveRetornarQuatroUsuariosIniciais() { // Dyonathan
        assertEquals(4, usuarioService.visualizarTodos().size());
    }

    // REQUISITO: Alterar níveis de acesso (Admin)
    @Test
    void alterarNivelAcessoDeveMudarOModeloDeUsuario() { // Dyonathan
        assertTrue(usuarioService.alterarNivelAcesso("u3", PapelUsuario.PROFESSOR));
        List<Usuario> users= dataManager.getUsuarios();
        for(Usuario user : users) {
            if (user.getId().equals("u3")) {
                assertEquals(PapelUsuario.PROFESSOR, user.getPapel());
            }
        }
    }

    @Test
    void alterarNivelAcessoInexistenteDeveFalhar() { // Dyonathan
        assertFalse(usuarioService.alterarNivelAcesso("bah, tá loko", PapelUsuario.PROFESSOR));
    }

    // REQUISITO: Filtrar e buscar usuários por nome, email ou papel (Admin)
    @Test
    void buscarUsuariosDeveFiltrarPorNome() { // Guilherme
        // TODO: Testar a busca de usuários por parte do nome (ex: "ana"):
        // 1. Chamar buscarUsuarios("ana", null).
        List<Usuario> usuariosFiltrados = usuarioService.buscarUsuarios("ana", null);
        // 2. Verificar se a lista retornada tem o tamanho correto (1) e o nome do usuário está correto.
        assertEquals(1, usuariosFiltrados.size());
    }

    @Test
    void buscarUsuariosDeveFiltrarPorEmail() { // Guilherme
        // TODO: Testar a busca de usuários por parte do email (ex: "@codefolio.com"):
        // 1. Chamar buscarUsuarios("@codefolio.com", null).
        List<Usuario> usuariosFiltrados = usuarioService.buscarUsuarios("@codefolio.com", null);
        // 2. Verificar se a lista retornada tem o tamanho correto (4).
        assertEquals(4, usuariosFiltrados.size());
    }

    @Test
    void buscarUsuariosDeveFiltrarPorPapel() { // Guilherme
        // TODO: Testar a busca de usuários por PapelUsuario (ex: PROFESSOR):
        // 1. Chamar buscarUsuarios(null, PapelUsuario.PROFESSOR).
        List<Usuario> usuariosFiltrados = usuarioService.buscarUsuarios(null, PapelUsuario.PROFESSOR);
        // 2. Verificar se a lista retornada tem o tamanho correto (1) e o papel do usuário está correto.
        assertEquals(1, usuariosFiltrados.size());
    }

    // REQUISITO: Ordenar usuários por diferentes critérios (Admin)
    @Test
    void ordenarUsuariosPorNomeDeveFuncionar() { // Luiz
        // TODO: Testar a ordenação de usuários por nome:
        // 1. Chamar ordenarUsuarios("nome").
        // 2. Verificar se os primeiros elementos da lista estão na ordem alfabética esperada (ex: "Ana Admin", "Bruno Professor").
    }

    @Test
    void ordenarUsuariosPorPapelDeveFuncionar() { // Luiz
        // TODO: Testar a ordenação de usuários por papel:
        // 1. Chamar ordenarUsuarios("papel").
        // 2. Verificar se os primeiros elementos da lista estão na ordem de papel esperada (ex: USUARIO_COMUM, ESTUDANTE).
    }

    // REQUISITO: Editar informações pessoais (Usuário Comum)
    @Test
    void editarPerfilDeveMudarONomeDoUsuario() { // Luiz
        // TODO: Testar a edição de informações de perfil (ex: mudar o nome do "u4"):
        // 1. Chamar editarPerfil() e verificar se retorna 'true'.
        // 2. Recuperar o usuário na persistência (dataManager).
        // 3. Verificar se o nome do usuário foi atualizado corretamente.
    }
}