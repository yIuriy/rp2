import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.PapelUsuario;
import model.Usuario;
import service.JsonDataManager;
import service.UsuarioService;

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
        List<Usuario> usuariosOrdenados = usuarioService.ordenarUsuarios("nome"); 
        assertNotNull(usuariosOrdenados); 
        assertTrue(usuariosOrdenados.size() >= 2); 
        assertEquals("Ana Admin", usuariosOrdenados.get(0).getNome()); 
        assertEquals("Bruno Professor", usuariosOrdenados.get(1).getNome()); 
        assertTrue(usuariosOrdenados.get(0).getNome().compareTo(usuariosOrdenados.get(1).getNome()) < 0);
    } 

    @Test
    void ordenarUsuariosPorPapelDeveFuncionar() { // Luiz
        List<Usuario> usuariosOrdenados = usuarioService.ordenarUsuarios("papel");
        assertNotNull(usuariosOrdenados); 
        assertTrue(usuariosOrdenados.size() >= 4); 
        assertEquals(PapelUsuario.USUARIO_COMUM, usuariosOrdenados.get(0).getPapel()); 
        assertEquals("Daniel Comum", usuariosOrdenados.get(0).getNome()); 
        assertEquals(PapelUsuario.ESTUDANTE, usuariosOrdenados.get(1).getPapel()); 
        assertEquals("Carla Estudante", usuariosOrdenados.get(1).getNome()); 
        assertEquals(PapelUsuario.PROFESSOR, usuariosOrdenados.get(2).getPapel()); 
        assertEquals(PapelUsuario.ADMINISTRADOR, usuariosOrdenados.get(3).getPapel()); 
    }
    

    // REQUISITO: Editar informações pessoais (Usuário Comum)
    @Test
    void editarPerfilDeveMudarONomeDoUsuario() { // Luiz
        String idUsuarioParaEditar = "u4"; 
        String novoNome = "Daniel Comum Editado";
        boolean sucesso = usuarioService.editarPerfil(idUsuarioParaEditar, novoNome); 
        assertTrue(sucesso);
        Optional<Usuario> usuarioOptional = dataManager.getUsuarios().stream() 
                .filter(u -> u.getId().equals(idUsuarioParaEditar)) 
                .findFirst(); 
        assertTrue(usuarioOptional.isPresent()); 
        Usuario usuarioDepois = usuarioOptional.get(); 
        assertEquals(novoNome, usuarioDepois.getNome()); 
    }
}