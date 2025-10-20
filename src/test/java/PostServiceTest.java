import model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.JsonDataManager;
import service.PostService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class PostServiceTest {

    private JsonDataManager dataManager;
    private PostService postService;

    @BeforeEach
    void setUp() {
        this.dataManager = new JsonDataManager();
        this.postService = new PostService(dataManager);
    }

    // REQUISITO: Visualizar posts/artigos (Usuário Comum)
    @Test
    void visualizarTodosDeveRetornarTodosOsPostsIniciais() { // Iuri
        List<Post> todosPostsRetornados = postService.visualizarTodos();
        assertEquals(2, todosPostsRetornados.size());
    }

    // REQUISITO: Remover posts inadequados (Administrador)
    @Test
    void removerPostDeveRemoverPostExistente() { // Iuri
        int sizeTodosPostsInicial = postService.visualizarTodos().size();
        boolean postRemovido = postService.removerPost("p1");
        assertTrue(postRemovido);
        int sizeTodosPostFinal = postService.visualizarTodos().size();
        assertEquals(sizeTodosPostsInicial - 1, sizeTodosPostFinal);
        boolean aindaEstaPresente = dataManager.getPosts().stream().anyMatch(
                post -> post.getId().equals("p1")
        );
        assertFalse(aindaEstaPresente);
    }

    @Test
    void removerPostInexistenteDeveRetornarFalso() { // Iuri
        // TODO: Testar a remoção de um post que não existe:
        boolean postRemovido = postService.removerPost("p999");
        assertFalse(postRemovido);
    }

    // REQUISITO: Curtir/descurtir posts (Usuário Comum)
    @Test
    void curtirPostDeveIncrementarContadorDeCurtidas() { // Gustavo
        Optional<Post> postOpcional = dataManager.getPosts().stream()
                .filter(post -> post.getId().equals("p2"))
                .findFirst();
        assertTrue(postOpcional.isPresent());
        Post post = postOpcional.get();
        int curtidasIniciais = post.getCurtidas();
    }

    // REQUISITO: Filtrar artigos por temas/tags (Usuário Comum)
    @Test
    void filtrarPorTagDeveRetornarPostsComTagCorrespondente() { // Gustavo
        List<Post> filtrados = postService.filtrarPorTag("Java");
        assertEquals(1, filtrados.size());
        assertEquals("Novidades do Java 21", filtrados.get(0).getTitulo());
    }

    @Test
    void filtrarPorTagDeveIgnorarCaseSensitivity() { // Gustavo
        List<Post> filtradosMinusculo = postService.filtrarPorTag("java");
        List<Post> filtradosMaiusculo = postService.filtrarPorTag("JAVA");

        assertEquals(1, filtradosMinusculo.size());
        assertEquals("Novidades do Java 21", filtradosMinusculo.get(0).getTitulo());

        assertEquals(1, filtradosMaiusculo.size());
        assertEquals("Novidades do Java 21", filtradosMaiusculo.get(0).getTitulo());
    }

    @Test
    void filtrarPorTagInexistenteDeveRetornarListaVazia() { // Gustavo
        List<Post> filtrados = postService.filtrarPorTag("Python");
        assertTrue(filtrados.isEmpty());
    }

    // REQUISITO: Visualizar métricas de engajamento (Administrador)
    @Test
    void getCurtidasTotaisDeveRetornarSomaCorreta() { // Dyonathan
        assertEquals(2, postService.getCurtidasTotais());
        postService.curtirPost("p2");
        assertEquals(3, postService.getCurtidasTotais());
        postService.curtirPost("p1");
        assertEquals(4, postService.getCurtidasTotais());
    }
}