import model.Curso;
import model.StatusCurso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CursoService;
import service.JsonDataManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CursoServiceTest {

    private JsonDataManager dataManager;
    private CursoService cursoService;

    @BeforeEach
    void setUp() {
        this.dataManager = new JsonDataManager();
        this.cursoService = new CursoService(dataManager);
    }

    // REQUISITO: Criar novos cursos (Professor)
    @Test
    void criarCursoDeveAdicionarNovoCursoAPersistencia() { // Lucas
        // TODO: Testar a criação de um novo curso:
        // 1. Verificar se o novo curso não é nulo.
        // 2. Verificar se o título e a descrição estão corretos.
        // 3. Confirmar que o status inicial é PENDENTE_APROVACAO.
        // 4. Garantir que o tamanho da lista de cursos aumentou em 1.
    }

    // REQUISITO: Editar cursos existentes (Professor/Admin)
    @Test
    void editarCursoDeveAtualizarTituloEDescricao() { // Lucas
        // TODO: Testar a edição de um curso existente (ex: "c1"):
        // 1. Chamar editarCurso() e verificar se o retorno é 'true'.
        // 2. Recuperar o curso na persistência (dataManager).
        // 3. Verificar se o título e a descrição foram atualizados corretamente.
    }

    // REQUISITO: Configurar proteção por PIN de acesso (Professor)
    @Test
    void configurarPinDeveAdicionarPinAoCurso() { // Lucas
        // TODO: Testar a configuração de PIN em um curso (ex: "c1"):
        // 1. Chamar configurarPin() e verificar se o retorno é 'true'.
        // 2. Recuperar o curso na persistência (dataManager).
        // 3. Verificar se o PIN foi adicionado/configurado corretamente.
    }

    // REQUISITO: Aprovar/rejeitar cursos (Administrador)
    @Test
    void aprovarCursoDeveMudarStatusParaAtivo() { // Lucas
        // TODO: Testar a aprovação de um curso PENDENTE (ex: "c2"):
        // 1. Chamar aprovarCurso() e verificar se o retorno é 'true'.
        // 2. Recuperar o curso na persistência (dataManager).
        // 3. Verificar se o status do curso mudou para ATIVO.
    }

    @Test
    void rejeitarCursoDeveMudarStatusParaInativo() { // Raphael
        // TODO: Testar a rejeição de um curso (ex: "c1"):

        // 1. Chamar rejeitarCurso() e verificar se o retorno é 'true'.
        boolean cursoFoiRejeitado = cursoService.rejeitarCurso("c1");
        assertTrue(cursoFoiRejeitado);

        // 2. Recuperar o curso na persistência (dataManager).
        List<Curso> cursos = dataManager.getCursos();

        // 3. Verificar se o status do curso mudou para INATIVO.
        Curso cursoRejeitado = cursos.getFirst();
        assertEquals(StatusCurso.INATIVO, cursoRejeitado.getStatus());

    }

    // REQUISITO: Visualizar catálogo de cursos disponíveis (Estudante/Comum)
    @Test
    void visualizarCatalogoDeveRetornarApenasCursosAtivos() { // Raphael
        // TODO: Testar a visualização do catálogo:

        // 1. Chamar visualizarCatalogo().
        List<Curso> catalogo = cursoService.visualizarCatalogo();
        int tamanhoCatalogoInicial = catalogo.size();

        // 2. Verificar se a lista retornada contém APENAS cursos com StatusCurso.ATIVO.
        int count = 0;
        for (Curso curso : catalogo) {
            assertEquals(StatusCurso.ATIVO, curso.getStatus());
            if (curso.getStatus().equals(StatusCurso.ATIVO)) count++;
        }

        // 3. Verificar se o tamanho da lista está correto (baseado nos dados iniciais).
        assertEquals(tamanhoCatalogoInicial, count);
    }

    // REQUISITO: Ingressar em cursos (com inserção de PIN quando necessário)
    @Test
    void ingressarCursoComPinDeveFuncionarComPinCorreto() { // Raphael
        // TODO: Testar o ingresso em um curso com PIN (ex: "c2"):

        // 1. Garantir que o curso está ATIVO (configurar se necessário).
        List<Curso> cursos = dataManager.getCursos();
        cursos.get(1).setStatus(StatusCurso.ATIVO);

        // 2. Chamar ingressarCurso() com o PIN CORRETO.
        boolean ingressouNoCurso = cursoService.ingressarCurso("c2", "1234");

        // 3. Verificar se o retorno é 'true'.
        assertTrue(ingressouNoCurso);
    }

    @Test
    void ingressarCursoComPinDeveFalharComPinIncorreto() { // Raphael
        // TODO: Testar o ingresso em um curso com PIN (ex: "c2"):

        // 1. Garantir que o curso está ATIVO (configurar se necessário).
        List<Curso> cursos = dataManager.getCursos();
        cursos.get(1).setStatus(StatusCurso.ATIVO);

        // 2. Chamar ingressarCurso() com um PIN INCORRETO.
        boolean ingressouNoCurso = cursoService.ingressarCurso("c2", "4321");

        // 3. Verificar se o retorno é 'false'.
        assertFalse(ingressouNoCurso);
    }

    @Test
    void ingressarCursoSemPinDeveFuncionar() { // Iuri
        // TODO: Testar o ingresso em um curso SEM PIN (ex: "c1"):
        // 1. Chamar ingressarCurso() passando 'null' ou uma string vazia como PIN.
        boolean ingressouNoCurso = cursoService.ingressarCurso("c1", null);
        // 2. Verificar se o retorno é 'true'.
        assertTrue(ingressouNoCurso);
    }
}