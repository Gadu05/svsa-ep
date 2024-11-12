package com.teste.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;


import com.teste.model.enums.StatusOcorrencia;
import com.teste.model.enums.TipoOcorrencia;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name="ocorrencia")
public class Ocorrencia implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@NotBlank(message="O TIPO DE OCORRÊNCIA É OBRIGATÓRIO")
	@Enumerated(EnumType.STRING)
	private TipoOcorrencia tipo;
	
	@NotBlank(message="O TIPO DE OCORRÊNCIA É OBRIGATÓRIO")
	@Enumerated(EnumType.STRING)
	private StatusOcorrencia status;
	
	@NotBlank(message="POR FAVOR, PREENCHA A DESCRIÇÃO")
	private String descricao;
	
	@ManyToOne
	private Usuario remetente;
	
	@ManyToOne
	private Usuario destinatario;
	
	//@ManyToOne
	private Long unidade;
	
	//@ManyToOne
	private Long tenant;
	
	//@OneToMany
	//private ArrayList<Resposta> respostas;
	
	
	
	
	
	/*
	 * Datas de Criação e Modificação
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataModificacao;

	@PrePersist
	@PreUpdate
	public void configuraDatasCriacaoAlteracao() {
		this.setDataModificacao( new Date() );
				
		if (this.getDataCriacao() == null) {
			this.setDataCriacao( new Date() );
		}		
	}
	
}
