package estudoSpringData.estudoSpringBoot.controllers;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import estudoSpringData.estudoSpringBoot.model.Usuario;
import estudoSpringData.estudoSpringBoot.repository.UsuarioRepository;


@RestController
public class GreetingsController {
   
	
    @Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String greetingText(@PathVariable String name) {
        return "Hello " + name + "!";
    }

    @GetMapping(value = "listatodos")
    @ResponseBody/*Retorna os dados para o corpo da resposta*/
    public ResponseEntity<List<Usuario>> listaUsuario() {
    	List<Usuario> usuarios = usuarioRepository.findAll();
    	return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);/*Retorna Lista em JSON*/
    }
    
    @PostMapping(value = "salvar")/*mapeia url*/
    @ResponseBody/*Decrição da resposta*/
    public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario){/*Receber os dados da resposta para salvar*/
    	Usuario user = usuarioRepository.save(usuario);
    	
    	return new ResponseEntity<Usuario>(user, HttpStatus.CREATED);
    }
    
    @DeleteMapping(value = "delete")/*mapeia url*/
    @ResponseBody/*Decrição da resposta*/
    public ResponseEntity<String> delete(@RequestParam Long iduser){/*Receber os dados da respsota para Deletar*/
    	usuarioRepository.deleteById(iduser);
    	
    	return new ResponseEntity<String>("Usuário Deletado com Sucesso", HttpStatus.OK);
    }
    
    @GetMapping(value = "buscaruserid")/*mapeia url*/
    @ResponseBody/*Decrição da resposta*/
    public ResponseEntity<Usuario> buscar(@RequestParam(name = "iduser") Long iduser){/*Receber os dados da resposta para consultar*/
    Usuario usuario = usuarioRepository.findById(iduser).get();
    	
    	return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
    }
    
    @PutMapping(value = "atualizar")/*mapeia url*/
    @ResponseBody/*Decrição da resposta*/
    public ResponseEntity<?> atualizar(@RequestBody Usuario usuario){/*Receber os dados da resposta para salvar*/
    	if(usuario.getId() == null) {
    		return new ResponseEntity<String>("Id não informado para atualização", HttpStatus.OK);	
    	}
    	
    	Usuario user = usuarioRepository.saveAndFlush(usuario);
    	
    	return new ResponseEntity<Usuario>(user, HttpStatus.OK);
    }
    
}
