package com.ms_procedure;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import com.ms_procedure.model.Procedure;
import com.ms_procedure.repository.ProcedureRepository;
import com.ms_procedure.service.ProcedureService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("PROCEDURE")
class MsProcedureApplicationTests {

	@Mock
	private ProcedureRepository procedureRepository;

	@Mock
	private ProcedureService procedureService;

	Procedure procedure = new Procedure();

	Flux<Procedure> list;

	@BeforeAll
	void init() {
		System.out.println("INICIO DE PRUEBAS");
	}

	@BeforeEach
	void setUp(){
		MockitoAnnotations.initMocks(this);
		procedure.setId(1);
		procedure.setProcedureConfigId(1);
		procedure.setPhaseId(4);
		procedure.setPersonId(1);
		procedure.setStudentId(1);
		procedure.setBatch(0);
		procedure.setNote(15);
		procedure.setCollaboratorTypeId(1);
		procedure.setActive(true);
		list = Flux.just(procedure);
	}

	@Nested
	@DisplayName("CRUD")
	class crud {

		@Test
		@DisplayName("LISTAR")
		void findAll(){
			when(procedureRepository.findAll()).thenReturn(list);
			System.out.println(when(procedureRepository.findAll()).thenReturn(list));
			System.out.println("LISTA = " + procedure);
			assertNotEquals(null, list);
		}

		@Test
		@DisplayName("GUARDAR")
		void save(){
			procedureRepository.save(procedure);
			System.out.println("GUARDADO = " + procedure);
			assertNotEquals(null, list);
		}

		@Test
		@DisplayName("CONSOLIDAR FASE 5")
		void consolidate5(){
			System.out.println("LISTA ORIGINAL = " + procedure);
			procedure.setPhaseId(5);
			procedureRepository.save(procedure);
			System.out.println("LISTA CONSOLIDADA 5 = " + procedure);
			System.out.println("FASE CAMBIADO A = " + procedure.getPhaseId());
			assertNotNull(procedure.getPhaseId());
		}

		@Test
		@DisplayName("CONSOLIDAR FASE 6")
		void consolidate6(){
			System.out.println("LISTA ORIGINAL = " + procedure);
			procedure.setPhaseId(6);
			procedureRepository.save(procedure);
			System.out.println("LISTA CONSOLIDADA 6 = " + procedure);
			System.out.println("FASE CAMBIADO A = " + procedure.getPhaseId());
			assertNotNull(procedure.getPhaseId());
		}

		@Test
		@DisplayName("CONSOLIDAR FASE 7")
		void consolidate7(){
			System.out.println("LISTA ORIGINAL = " + procedure);
			procedure.setPhaseId(7);
			procedureRepository.save(procedure);
			System.out.println("LISTA CONSOLIDADA 7 = " + procedure);
			System.out.println("FASE CAMBIADO A = " + procedure.getPhaseId());
			assertNotNull(procedure.getPhaseId());
		}

	}

	@AfterAll
	void end() {
		System.out.println("FIN DE PRUEBAS");
	}

}
