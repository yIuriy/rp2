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
        // TODO: Testar a visualização de todos os posts:
        // 1. Chamar visualizarTodos().
        List<Post> todosPostsRetornados = postService.visualizarTodos();
        // 2. Verificar se a lista retornada tem o tamanho esperado (2 nos dados iniciais).
        assertEquals(2, todosPostsRetornados.size());
    }

    // REQUISITO: Remover posts inadequados (Administrador)
    @Test
    void removerPostDeveRemoverPostExistente() { // Iuri
        // TODO: Testar a remoção de um post existente (ex: "p1"):
        // 1. Armazenar o tamanho inicial da lista de posts.
        int sizeTodosPostsInicial = postService.visualizarTodos().size();
        // 2. Chamar removerPost("p1") e verificar se retorna 'true'.
        boolean postRemovido = postService.removerPost("p1");
        assertTrue(postRemovido);
        int sizeTodosPostFinal = postService.visualizarTodos().size();
        // 3. Verificar se o tamanho da lista diminuiu em 1.
        assertEquals(sizeTodosPostsInicial - 1, sizeTodosPostFinal);
        // 4. Garantir que o post "p1" não existe mais na persistência (dataManager).
        boolean aindaEstaPresente = dataManager.getPosts().stream().anyMatch(
                post -> post.getId().equals("p1")
        );
        assertFalse(aindaEstaPresente);
    }

    @Test
    void removerPostInexistenteDeveRetornarFalso() { // Iuri
        // TODO: Testar a remoção de um post que não existe:
        // 1. Chamar removerPost() com um ID inexistente (ex: "p999").
        boolean postRemovido = postService.removerPost("p999");
        // 2. Verificar se o retorno é 'false'.
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
        // TODO: Testar o cálculo do total de curtidas:
        // 1. Chamar getCurtidasTotais() e verificar se o total inicial é correto (2).
        // 2. Simular uma nova interação (ex: curtirPost("p2")).
        // 3. Chamar getCurtidasTotais() novamente e verificar se o novo total (3) está correto.
    }
}