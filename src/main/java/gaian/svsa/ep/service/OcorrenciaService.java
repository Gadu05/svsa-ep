package gaian.svsa.ep.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import gaian.svsa.ep.dao.OcorrenciaDAO;
import gaian.svsa.ep.dao.RespostaDAO;
import gaian.svsa.ep.model.Ocorrencia;
import gaian.svsa.ep.model.Resposta;
import gaian.svsa.ep.model.UsuarioEP;
import gaian.svsa.ep.model.enums.GrupoEP;
import gaian.svsa.ep.model.enums.StatusOcorrencia;
import gaian.svsa.ep.util.jpa.Transactional;
import lombok.extern.log4j.Log4j;

@Log4j
public class OcorrenciaService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private OcorrenciaDAO ocorrenciaDAO;
	
	@Inject
	private RespostaDAO respostaDAO;
	
	public Ocorrencia buscarPorId(Long id) {

		log.info("Buscando ocorrencia por Id");

		return this.ocorrenciaDAO.buscarPeloCodigo(id);
	}
	
	public List<Resposta> buscarTodasRespostas(Ocorrencia ocorrencia) {
		
		return this.respostaDAO.buscarTodasPorOcorrencia(ocorrencia);
		
	}
	
	public List<Ocorrencia> buscarTodasPendencias (UsuarioEP usuario) {
		return this.ocorrenciaDAO.buscarPendenciasPorUsuario(usuario);
	}
	
	@Transactional
	public void novaResposta(Resposta resposta) {
	    try {
	        this.respostaDAO.salvar(resposta);

	        Ocorrencia ocorrencia = resposta.getOcorrencia();
	        if (resposta.getUsuario().getGrupo() == GrupoEP.COORDENADORES && ocorrencia.getStatus() == StatusOcorrencia.COORDENADOR) {
	            ocorrencia.setStatus(StatusOcorrencia.GESTOR);
	            log.info("Coordenador criando nova ocorrência: " + ocorrencia);
	            this.ocorrenciaDAO.salvar(ocorrencia);
	        }
	    } catch (Exception e) {
	        log.error("Erro", e);

	    }
	}
	
	@Transactional
	public void gerirOcorrencia (Ocorrencia ocorrencia) {
	    try {
	    	
	        this.ocorrenciaDAO.salvar(ocorrencia);
	       
	    } catch (Exception e) {
	        log.error("Erro", e);

	    }
	}
		
	}
	