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
        // TODO: Testar a função de curtir post (ex: "p2"):
        // 1. Obter o número de curtidas iniciais do post.
        // 2. Chamar curtirPost("p2") e verificar se retorna 'true'.
        // 3. Verificar se o contador de curtidas do post na persistência (dataManager) aumentou em 1.
    }

    // REQUISITO: Filtrar artigos por temas/tags (Usuário Comum)
    @Test
    void filtrarPorTagDeveRetornarPostsComTagCorrespondente() { // Gustavo
        // TODO: Testar a filtragem por uma tag existente (ex: "Java"):
        // 1. Chamar filtrarPorTag() com a tag.
        // 2. Verificar se a lista retornada tem o tamanho esperado (1).
        // 3. Verificar se o post retornado é o correto (ex: "Novidades do Java 21").
    }

    @Test
    void filtrarPorTagDeveIgnorarCaseSensitivity() { // Gustavo
        // TODO: Testar a filtragem com uma tag existente, usando case insensível (ex: "java"):
        // 1. Chamar filtrarPorTag() com a tag em minúsculas ou maiúsculas.
        // 2. Verificar se o resultado é o mesmo do teste anterior.
    }

    @Test
    void filtrarPorTagInexistenteDeveRetornarListaVazia() { // Gustavo
        // TODO: Testar a filtragem por uma tag que não existe (ex: "Python"):
        // 1. Chamar filtrarPorTag() com a tag inexistente.
        // 2. Verificar se a lista retornada está vazia (isEmpty() == true).
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