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
        int tamanhoInicial = dataManager.getCursos().size();
        String titulo = "Curso de Testes Unitários";
        String desc = "Aprendendo JUnit 5";
        String profId = "prof-junit";

        Curso novoCurso = cursoService.criarCurso(titulo, desc, profId);

        assertNotNull(novoCurso);
        assertNotNull(novoCurso.getId());

        Curso cursoPersistido = dataManager.getCursoById(novoCurso.getId());
        assertNotNull(cursoPersistido);

        assertEquals(titulo, cursoPersistido.getTitulo());
        assertEquals(desc, cursoPersistido.getDescricao());

        assertEquals(StatusCurso.PENDENTE_APROVACAO, cursoPersistido.getStatus());

        int tamanhoFinal = dataManager.getCursos().size();
        assertEquals(tamanhoInicial + 1, tamanhoFinal);
    }

    // REQUISITO: Editar cursos existentes (Professor/Admin)
    @Test
    void editarCursoDeveAtualizarTituloEDescricao() { // Lucas
        Curso cursoBase = cursoService.criarCurso("Título Antigo", "Descrição Antiga", "prof-1");
        String idCurso = cursoBase.getId();
        String novoTitulo = "Título Atualizado";
        String novaDescricao = "Descrição Atualizada";

        boolean sucesso = cursoService.editarCurso(idCurso, novoTitulo, novaDescricao);

        assertTrue(sucesso);

        Curso cursoAtualizado = dataManager.getCursoById(idCurso);
        assertNotNull(cursoAtualizado);

        assertEquals(novoTitulo, cursoAtualizado.getTitulo());
        assertEquals(novaDescricao, cursoAtualizado.getDescricao());
    }

    // REQUISITO: Configurar proteção por PIN de acesso (Professor)
    @Test
    void configurarPinDeveAdicionarPinAoCurso() { // Lucas
        Curso cursoBase = cursoService.criarCurso("Curso Protegido", "Desc", "prof-2");
        String idCurso = cursoBase.getId();
        String novoPin = "9876";

        assertNull(cursoBase.getPinAcesso());

        boolean sucesso = cursoService.configurarPin(idCurso, novoPin);
        assertTrue(sucesso);

        Curso cursoAtualizado = dataManager.getCursoById(idCurso);
        assertNotNull(cursoAtualizado);

        assertEquals(novoPin, cursoAtualizado.getPinAcesso());
    }

    // REQUISITO: Aprovar/rejeitar cursos (Administrador)
    @Test
    void aprovarCursoDeveMudarStatusParaAtivo() { // Lucas
        Curso cursoBase = cursoService.criarCurso("Curso Pendente", "Aguardando Aprovação", "prof-3");
        String idCurso = cursoBase.getId();

        assertEquals(StatusCurso.PENDENTE_APROVACAO, cursoBase.getStatus());

        boolean sucesso = cursoService.aprovarCurso(idCurso);

        assertTrue(sucesso);

        Curso cursoAtualizado = dataManager.getCursoById(idCurso);
        assertNotNull(cursoAtualizado);

        assertEquals(StatusCurso.ATIVO, cursoAtualizado.getStatus());
    }

    @Test
    void rejeitarCursoDeveMudarStatusParaInativo() { // Raphael
        boolean cursoFoiRejeitado = cursoService.rejeitarCurso("c1");
        assertTrue(cursoFoiRejeitado);

        List<Curso> cursos = dataManager.getCursos();

        Curso cursoRejeitado = cursos.getFirst();
        assertEquals(StatusCurso.INATIVO, cursoRejeitado.getStatus());

    }

    // REQUISITO: Visualizar catálogo de cursos disponíveis (Estudante/Comum)
    @Test
    void visualizarCatalogoDeveRetornarApenasCursosAtivos() { // Raphael
        List<Curso> catalogo = cursoService.visualizarCatalogo();
        int tamanhoCatalogoInicial = catalogo.size();

        int count = 0;
        for (Curso curso : catalogo) {
            assertEquals(StatusCurso.ATIVO, curso.getStatus());
            if (curso.getStatus().equals(StatusCurso.ATIVO)) count++;
        }

        assertEquals(tamanhoCatalogoInicial, count);
    }

    // REQUISITO: Ingressar em cursos (com inserção de PIN quando necessário)
    @Test
    void ingressarCursoComPinDeveFuncionarComPinCorreto() { // Raphael
        List<Curso> cursos = dataManager.getCursos();
        cursos.get(1).setStatus(StatusCurso.ATIVO);

        boolean ingressouNoCurso = cursoService.ingressarCurso("c2", "1234");

        assertTrue(ingressouNoCurso);
    }

    @Test
    void ingressarCursoComPinDeveFalharComPinIncorreto() { // Raphael
        List<Curso> cursos = dataManager.getCursos();
        cursos.get(1).setStatus(StatusCurso.ATIVO);

        boolean ingressouNoCurso = cursoService.ingressarCurso("c2", "4321");

        assertFalse(ingressouNoCurso);
    }

    @Test
    void ingressarCursoSemPinDeveFuncionar() { // Iuri
        // TODO: Testar o ingresso em um curso SEM PIN (ex: "c1"):
        boolean ingressouNoCurso = cursoService.ingressarCurso("c1", null);
        assertTrue(ingressouNoCurso);
    }
}