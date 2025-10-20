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
    private Optional<Curso> findCursoInList(String id) {
        return dataManager.getCursos().stream()
                .filter(curso -> curso.getId().equals(id))
                .findFirst();
    }

    @Test
    void criarCursoDeveAdicionarNovoCursoAPersistencia() { // Lucas
        int tamanhoInicial = dataManager.getCursos().size();
        String titulo = "Curso de Testes Unitários";
        String desc = "Aprendendo JUnit 5";
        String profId = "prof-junit";

        Curso novoCurso = cursoService.criarCurso(titulo, desc, profId);

        assertNotNull(novoCurso);
        assertEquals("c4", novoCurso.getId());

        Optional<Curso> cursoPersistidoOpt = findCursoInList(novoCurso.getId());
        assertTrue(cursoPersistidoOpt.isPresent(), "Curso criado não foi encontrado na persistência.");
        Curso cursoPersistido = cursoPersistidoOpt.get();

        assertEquals(titulo, cursoPersistido.getTitulo());
        assertEquals(desc, cursoPersistido.getDescricao());

        assertEquals(StatusCurso.PENDENTE_APROVACAO, cursoPersistido.getStatus());

        int tamanhoFinal = dataManager.getCursos().size();
        assertEquals(tamanhoInicial + 1, tamanhoFinal);
    }

    @Test
    void editarCursoDeveAtualizarTituloEDescricao() { // Lucas
        String idCurso = "c1";
        String novoTitulo = "Título Atualizado";
        String novaDescricao = "Descrição Atualizada";

        boolean sucesso = cursoService.editarCurso(idCurso, novoTitulo, novaDescricao);

        assertTrue(sucesso, "Método editarCurso() deveria retornar true.");

        Optional<Curso> cursoAtualizadoOpt = findCursoInList(idCurso);
        assertTrue(cursoAtualizadoOpt.isPresent(), "Curso 'c1' não foi encontrado.");
        Curso cursoAtualizado = cursoAtualizadoOpt.get();

        assertEquals(novoTitulo, cursoAtualizado.getTitulo());
        assertEquals(novaDescricao, cursoAtualizado.getDescricao());
    }

    @Test
    void configurarPinDeveAdicionarPinAoCurso() { // Lucas
        String idCurso = "c1";
        String novoPin = "9876";

        Optional<Curso> cursoBaseOpt = findCursoInList(idCurso);
        assertTrue(cursoBaseOpt.isPresent());
        assertNull(cursoBaseOpt.get().getPinAcesso(), "PIN de 'c1' deveria ser nulo inicialmente.");

        boolean sucesso = cursoService.configurarPin(idCurso, novoPin);

        assertTrue(sucesso, "Método configurarPin() deveria retornar true.");

        Optional<Curso> cursoAtualizadoOpt = findCursoInList(idCurso);
        assertTrue(cursoAtualizadoOpt.isPresent(), "Curso 'c1' não foi encontrado.");

        assertEquals(novoPin, cursoAtualizadoOpt.get().getPinAcesso());
    }

    @Test
    void aprovarCursoDeveMudarStatusParaAtivo() { // Lucas
        String idCurso = "c2";

        Optional<Curso> cursoBaseOpt = findCursoInList(idCurso);
        assertTrue(cursoBaseOpt.isPresent());
        assertEquals(StatusCurso.PENDENTE_APROVACAO, cursoBaseOpt.get().getStatus(), "Status inicial de 'c2' deveria ser PENDENTE.");

        boolean sucesso = cursoService.aprovarCurso(idCurso);

        assertTrue(sucesso, "Método aprovarCurso() deveria retornar true.");

        Optional<Curso> cursoAtualizadoOpt = findCursoInList(idCurso);
        assertTrue(cursoAtualizadoOpt.isPresent(), "Curso 'c2' não foi encontrado.");

        assertEquals(StatusCurso.ATIVO, cursoAtualizadoOpt.get().getStatus());
    }
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