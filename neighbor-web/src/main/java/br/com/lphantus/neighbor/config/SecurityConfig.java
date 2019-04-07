package br.com.lphantus.neighbor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuração spring security
 * 
 * @author elias.policena@lphantus.com.br
 * @since 08/11/2014
 *
 */
@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
@EnableGlobalAuthentication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String ROLE_USER = "ROLE_USER",
			ROLE_ROOT = "ROLE_ROOT",
			ROLE_PAGINA_MORADOR = "ROLE_PAGINA_MORADOR",
			ROLE_CADASTRO_CONDOMINIO = "ROLE_CADASTRO_CONDOMINIO",
			ROLE_CADASTRO_ITEM_RESERVA = "ROLE_CADASTRO_ITEM_RESERVA",
			ROLE_CADASTRO_MODULOACESSO = "ROLE_CADASTRO_MODULOACESSO",
			ROLE_CADASTRO_SINDICO = "ROLE_CADASTRO_SINDICO",
			ROLE_CADASTRO_USUARIO = "ROLE_CADASTRO_USUARIO",
			ROLE_LISTA_CONDOMINIO = "ROLE_LISTA_CONDOMINIO",
			ROLE_LISTA_ITEM_RESERVA = "ROLE_LISTA_ITEM_RESERVA",
			ROLE_LISTA_MODULOACESSO = "ROLE_LISTA_MODULOACESSO",
			ROLE_LISTA_MORADOR_ADM = "ROLE_LISTA_MORADOR_ADM",
			ROLE_LISTA_SINDICO = "ROLE_LISTA_SINDICO",
			ROLE_LISTA_USUARIO = "ROLE_LISTA_USUARIO",
			ROLE_REGISTRO_PAGAMENTO = "ROLE_REGISTRO_PAGAMENTO",
			ROLE_LISTA_MENSAGEM = "ROLE_LISTA_MENSAGEM",
			ROLE_MENSAGEM_DO_SINDICO = "ROLE_MENSAGEM_DO_SINDICO",
			ROLE_MENSAGEM_PARA_SINDICO = "ROLE_MENSAGEM_PARA_SINDICO",
			ROLE_CADASTRO_ANIMAL = "ROLE_CADASTRO_ANIMAL",
			ROLE_CADASTRO_MORADOR = "ROLE_CADASTRO_MORADOR",
			ROLE_CADASTRO_PRESTADOR = "ROLE_CADASTRO_PRESTADOR",
			ROLE_CADASTRO_VEICULO = "ROLE_CADASTRO_VEICULO",
			ROLE_CADASTRO_VISITANTE = "ROLE_CADASTRO_VISITANTE",
			ROLE_CADASTRO_MARCA_VEICULO = "ROLE_CADASTRO_MARCA_VEICULO",
			ROLE_CADASTRO_TIPO_ANIMAL = "ROLE_CADASTRO_TIPO_ANIMAL",
			ROLE_CADASTRO_RESERVA = "ROLE_CADASTRO_RESERVA",
			ROLE_LISTA_AGREGADOS = "ROLE_LISTA_AGREGADOS",
			ROLE_LISTA_ANIMAL = "ROLE_LISTA_ANIMAL",
			ROLE_LISTA_MORADOR = "ROLE_LISTA_MORADOR",
			ROLE_LISTA_OCORRENCIA = "ROLE_LISTA_OCORRENCIA",
			ROLE_LISTA_PRESTADOR = "ROLE_LISTA_PRESTADOR",
			ROLE_LISTA_RESERVA = "ROLE_LISTA_RESERVA",
			ROLE_LISTA_VEICULOS = "ROLE_LISTA_VEICULOS",
			ROLE_LISTA_VISITANTE = "ROLE_LISTA_VISITANTE",
			ROLE_RELATORIO = "ROLE_RELATORIO";

	@Autowired
	@Qualifier("neighborUserDetailsService")
	private UserDetailsService userDetailsService;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		AuthenticationManager bean = super.authenticationManagerBean();
		return bean;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(
				passwordEncoder());
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(
				passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.antMatchers("/")
				.hasAnyAuthority(ROLE_ROOT, ROLE_USER)
				.antMatchers("/index.jsp")
				.hasAnyAuthority(ROLE_ROOT, ROLE_USER)
				.antMatchers("/login_neighbor")
				.permitAll()
				.antMatchers("/pages/acesso/login.jsf*")
				.permitAll()
				.antMatchers("/pages/acesso/esquecisenha.jsf*")
				.permitAll()
				.antMatchers("/pages/acesso/paginaNaoEncontrada.jsf*")
				.permitAll()
				.antMatchers("/pages/acesso/home.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_USER, ROLE_PAGINA_MORADOR)
				.antMatchers("/pages/acesso/acessoNegado.jsf")
				.permitAll()
				.antMatchers("/pages/acesso/redefinirsenha.jsf*")
				.permitAll()
				.antMatchers("/pages/administracao/cadcondominio.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_CADASTRO_CONDOMINIO)
				.antMatchers("/pages/administracao/caditemreserva.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_CADASTRO_ITEM_RESERVA)
				.antMatchers("/pages/administracao/cadmoduloacesso.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_CADASTRO_MODULOACESSO)
				.antMatchers("/pages/administracao/cadsindico.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_CADASTRO_SINDICO)
				.antMatchers("/pages/administracao/cadusuario.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_CADASTRO_USUARIO)
				.antMatchers("/pages/administracao/listacondominio.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_CONDOMINIO)
				.antMatchers("/pages/administracao/listaitemreserva.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_ITEM_RESERVA)
				.antMatchers("/pages/administracao/listamoduloacesso.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_MODULOACESSO)
				.antMatchers("/pages/administracao/listamoradoradm.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_MORADOR_ADM)
				.antMatchers("/pages/administracao/listasindicos.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_SINDICO)
				.antMatchers("/pages/administracao/listausuario.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_USUARIO)
				.antMatchers("/pages/administracao/registropagamentos.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_REGISTRO_PAGAMENTO)
				.antMatchers("/pages/comunicacao/listamensagens.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_MENSAGEM)
				.antMatchers("/pages/comunicacao/mensagemdosindico.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_MENSAGEM_DO_SINDICO)
				.antMatchers("/pages/comunicacao/mensagemsindico.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_MENSAGEM_PARA_SINDICO,
						ROLE_PAGINA_MORADOR)
				.antMatchers("/pages/cadastros/cadanimalestimacao.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_CADASTRO_ANIMAL)
				.antMatchers("/pages/cadastros/cadmorador.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_CADASTRO_MORADOR)
				.antMatchers("/pages/cadastros/cadprestador.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_CADASTRO_PRESTADOR)
				.antMatchers("/pages/cadastros/cadveiculo.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_CADASTRO_VEICULO)
				.antMatchers("/pages/cadastros/cadvisitante.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_CADASTRO_VISITANTE)
				.antMatchers("/pages/cadastros/manutencaomarca.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_CADASTRO_MARCA_VEICULO)
				.antMatchers("/pages/cadastros/manutencaotipoanimal.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_CADASTRO_TIPO_ANIMAL)
				.antMatchers("/pages/cadastros/reserva.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_CADASTRO_RESERVA,
						ROLE_PAGINA_MORADOR)
				.antMatchers("/pages/listagem/listaagregados.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_AGREGADOS)
				.antMatchers("/pages/listagem/listaanimais.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_ANIMAL)
				.antMatchers("/pages/listagem/listamorador.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_MORADOR)
				.antMatchers("/pages/listagem/listaocorrencias.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_OCORRENCIA)
				.antMatchers("/pages/listagem/listaprestador.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_PRESTADOR)
				.antMatchers("/pages/listagem/listareserva.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_RESERVA,
						ROLE_PAGINA_MORADOR)
				.antMatchers("/pages/listagem/listaveiculos.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_VEICULOS)
				.antMatchers("/pages/listagem/listavisitante.jsf*")
				.hasAnyAuthority(ROLE_ROOT, ROLE_LISTA_VISITANTE)
				.antMatchers("/pages/root/**").hasAnyAuthority(ROLE_ROOT)
				.antMatchers("/pages/morador/**")
				.hasAnyAuthority(ROLE_ROOT, ROLE_PAGINA_MORADOR)
				.antMatchers("/pages/relatorios/**")
				.hasAnyAuthority(ROLE_ROOT, ROLE_RELATORIO).anyRequest()
				.authenticated().and().formLogin()
				.loginPage("/pages/acesso/login.jsf")
				.failureUrl("/pages/acesso/login.jsf?error=invalido")
				.defaultSuccessUrl("/", true)
				.usernameParameter("neighbor_usuario")
				.passwordParameter("neighbor_password")
				.loginProcessingUrl("/login_neighbor").and().logout()
				.logoutSuccessUrl("/pages/acesso/login.jsf").and()
				.exceptionHandling()
				.accessDeniedPage("/pages/acesso/acessoNegado.jsf").and()
				.httpBasic().and().csrf().disable().headers().disable();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new br.com.lphantus.neighbor.config.security.PasswordEncoder();
		return encoder;
	}

}
