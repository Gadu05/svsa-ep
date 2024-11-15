package com.svsa.ep.controller;

import java.io.IOException;
import java.io.Serializable;

import org.primefaces.model.menu.MenuModel;

import com.svsa.ep.model.UsuarioEP;
import com.svsa.ep.service.AutenticacaoService;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import lombok.*;
import lombok.extern.log4j.Log4j;


@Getter
@Setter
@Log4j
@Named
@SessionScoped
public class AutenticacaoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	

	@Inject
    private AutenticacaoService autenticacaoService;
	
	private UsuarioEP usuario = null;	
	private MenuModel menu = null;
	
	
	public void autenticar() throws IOException {
		log.info("entrar com o usuário: ");
		
		FacesMessage message = null;       
		
		try {
	
			if(usuario == null) {
				
				HttpServletRequest request = this.getRequest();
				String idCriptografado = this.getCookie(request);
										
				this.usuario = this.autenticacaoService.autenticar(idCriptografado);				
					
					if(usuario != null) {
						
						log.info("Bem vindo " + usuario.getNome() + "!");	
						this.criarMenu();
					
					}
				
				 else {
					log.info("usuario não está logado, será redirecionado ");
					try {
						FacesContext context = FacesContext.getCurrentInstance();
						context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/restricted/home/SvsaHome.xhtml");
					} catch (IOException ioException) {
						log.error("Erro ao redirecionar: " + ioException.getMessage(), ioException);
					}
				}
			}
		} catch (NoResultException e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro de autenticação ", "Verifique se está logado.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} 
		
	}
	
	public String getCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("SESSIONID".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	

 
   public HttpServletRequest getRequest() {	
	   HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		
		return request;
	}
  

   public void criarMenu() {
       this.menu = autenticacaoService.criarMenu(this.usuario);
	}
  

}