# Spring Boot REST API

Estudando os conceitos de programação REST API

#### Entity
Criando o entity na camada Model e criando tabela no banco de dados
~~~JAVA
@Entity
@SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1, initialValue = 1)
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
	private Long id;

	private String nome;

	private int idade;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

}
~~~

![Criando tabela](https://user-images.githubusercontent.com/101072311/199089515-e8097250-ac55-4267-a4ed-5024dce1133d.png)

#### Criando o Repository

~~~JAVA
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
~~~

#### Fazendo o injeção de dependência
Fazendo a injeção dependência no Controller.

~~~JAVA
@Autowired
	private UsuarioRepository usuarioRepository;
~~~

## EndPoint

#### Listando Todos(GET)
Criando o EndPoint que lista todos usuários e testando no Postman.
~~~JAVA
	@GetMapping(value = "listatodos")
    @ResponseBody/*Retorna os dados para o corpo da resposta*/
    public ResponseEntity<List<Usuario>> listaUsuario() {
    	List<Usuario> usuarios = usuarioRepository.findAll();
    	return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);/*Retorna Lista em JSON*/
    }
~~~

![listaTodosPostman](https://user-images.githubusercontent.com/101072311/199598494-6c7bd801-206f-45be-9028-a74a74afaea7.png)

![lista todos banco](https://user-images.githubusercontent.com/101072311/199342656-29cdf1c1-06cc-4fd7-8723-98fe9b9e289b.png)

#### Salvando no banco(POST)
Criando o EndPoint que salva no banco de dados e testando no Postman.
~~~JAVA
@PostMapping(value = "salvar")/*mapeia url*/
    @ResponseBody/*Decrição da resposta*/
    public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario){/*Receber os dados da resposta para salvar*/
    	Usuario user = usuarioRepository.save(usuario);

    	return new ResponseEntity<Usuario>(user, HttpStatus.CREATED);
    }
~~~

![teste](https://user-images.githubusercontent.com/101072311/199606521-8df59512-f70a-443d-b85c-a58b9d7d0361.png)

#### DELETE
Criando o EndPoint para deletar do banco.

~~~JAVA
@DeleteMapping(value = "delete")/*mapeia url*/
    @ResponseBody/*Decrição da resposta*/
    public ResponseEntity<String> delete(@RequestParam Long iduser){/*Receber os dados da resposta para Deletar*/
    	usuarioRepository.deleteById(iduser);

    	return new ResponseEntity<String>("Usuário Deletado com Sucesso", HttpStatus.OK);
    }
~~~

![delete](https://user-images.githubusercontent.com/101072311/199610299-893ee562-d6cb-4b65-ae69-c6c70b313333.png)

![sucesso](https://user-images.githubusercontent.com/101072311/199610309-5ba55dc2-9de6-4b39-8370-ee5b0f4bee71.png)

#### Buscar Id
Criando o EndPoint para buscar.
~~~JAVA
@GetMapping(value = "buscaruserid")/*mapeia url*/
    @ResponseBody/*Decrição da resposta*/
    public ResponseEntity<Usuario> buscar(@RequestParam(name = "iduser") Long iduser){/*Receber os dados da respsota para consultar*/
    Usuario usuario = usuarioRepository.findById(iduser).get();

    	return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
    }
~~~

![buscapostman](https://user-images.githubusercontent.com/101072311/199612562-882f5e2c-c767-473d-aa45-02f89577dc21.png)

#### Atualizando no banco
Criando um EndPoint PUT para atualizar no banco de dados.

~~~JAVA
@PutMapping(value = "atualizar")/*mapeia url*/
    @ResponseBody/*Decrição da resposta*/
    public ResponseEntity<?> atualizar(@RequestBody Usuario usuario){/*Receber os dados da resposta para salvar*/
    	if(usuario.getId() == null) {
    		return new ResponseEntity<String>("Id não informado para atualização", HttpStatus.OK);
    	}

    	Usuario user = usuarioRepository.saveAndFlush(usuario);

    	return new ResponseEntity<Usuario>(user, HttpStatus.OK);
    }
~~~

![antesatualizar](https://user-images.githubusercontent.com/101072311/199615291-d9eecf06-5aa8-4cce-a86a-a71958fa890e.png)

 __Atualizado__

![atualizado](https://user-images.githubusercontent.com/101072311/199615489-fb05adf9-a62c-4cf3-8104-2d5810be32de.png)

##### Caso não seja informado o Id na hora da atualização vai ocorrer o erro.

~~~JAVA
if(usuario.getId() == null) {
	return new ResponseEntity<String>("Id não informado para atualização", HttpStatus.OK);
}
~~~

![Id não informado](https://user-images.githubusercontent.com/101072311/199615319-e281e4bb-2251-44ca-8a27-1bc5b05f433f.png)

## Criando um formulário
Criando formulário utilizando HTML, JAVASCRIP, BOOTSTRAP e JQuery

#### Funcion Botão Deletar da Tela

~~~JAVA
function botaoDeletarDaTela() {
        var id = $('#id').val()

        if (id != null && id.trim() != '') {
          deleteUser(id)
          document.getElementById('formCadastroUser').reset()
        }
      }
~~~
#### Funcion Delete User

~~~JAVA
function deleteUser(id) {
        if (confirm('Deseja realmente deletar?')) {
          $.ajax({
            method: 'DELETE',
            url: 'delete',
            data: 'iduser=' + id,
            success: function (response) {
              $('#' + id).remove()

              alert(response)
            }
          }).fail(function (xhr, status, errorThrown) {
            alert('Erro ao deletar usuario por id: ' + xhr.responseText)
          })
        }
      }
~~~
#### Function Pesquisar User

~~~JAVA
function pesquisarUser() {
        var nome = $('#nameBusca').val()

        if (nome != null && nome.trim() != '') {
          $.ajax({
            method: 'GET',
            url: 'buscarPorNome',
            data: 'name=' + nome,
            success: function (response) {
              $('#tabelaresultados > tbody > tr').remove()

              for (var i = 0; i < response.length; i++) {
                $('#tabelaresultados > tbody').append(
                  '<tr id="' +
                    response[i].id +
                    '"><td>' +
                    response[i].id +
                    '</td><td>' +
                    response[i].nome +
                    '</td><td><button type="button" onclick="colocarEmEdicao(' +
                    response[i].id +
                    ')" class="btn btn-primary">Ver</button></td><td><button type="button" class="btn btn-danger" onclick="deleteUser(' +
                    response[i].id +
                    ')">Delete</button></td></tr>'
                )
              }
            }
          }).fail(function (xhr, status, errorThrown) {
            alert('Erro ao buscar usuario: ' + xhr.responseText)
          })
        }
      }
~~~
#### Function colocar em edição

~~~JAVA
function colocarEmEdicao(id) {
        $.ajax({
          method: 'GET',
          url: 'buscaruserid',
          data: 'iduser=' + id,
          success: function (response) {
            $('#id').val(response.id)
            $('#nome').val(response.nome)
            $('#idade').val(response.idade)

            $('#modalPesquisarUser').modal('hide')
          }
        }).fail(function (xhr, status, errorThrown) {
          alert('Erro ao buscar usuario por id: ' + xhr.responseText)
        })
      }
~~~
#### Function Salvar usuários

~~~JAVA
function salvarUsuario() {
        var id = $('#id').val()
        var nome = $('#nome').val()
        var idade = $('#idade').val()

        if (nome == null || (nome != null && nome.trim() == '')) {
          $('#nome').focus()
          alert('Informe o nome')
          return
        }

        if (idade == null || (idade != null && idade.trim() == '')) {
          $('#idade').focus()
          alert('Informe a idade')
          return
        }

        $.ajax({
          method: 'POST',
          url: 'salvar',
          data: JSON.stringify({
            id: id,
            nome: nome,
            idade: idade
          }),
          contentType: 'application/json; charset=utf-8',
          success: function (response) {
            $('#id').val(response.id)
            alert('Gravou com sucesso!')
          }
        }).fail(function (xhr, status, errorThrown) {
          alert('Erro ao salvar usuario: ' + xhr.responseText)
        })
      }
~~~

![Tela Home](https://user-images.githubusercontent.com/101072311/199790305-d886da4a-02d3-4941-b8d7-c16859322e74.png)

![telapesquisa](https://user-images.githubusercontent.com/101072311/199790265-5051a856-7208-4c03-a01d-6c6ea2f22adf.png)

## Ferramentas e Tecnologias usadas nesse repositório 🌐
<div style="display: inline_block"><br>

<img align="center" alt="Augusto-Java" height="60" width="60" src=https://github.com/devicons/devicon/blob/master/icons/java/java-original.svg >
<img align="center" alt="Augusto-SpringBoot" height="60" width="60" src="https://raw.githubusercontent.com/devicons/devicon/1119b9f84c0290e0f0b38982099a2bd027a48bf1/icons/spring/spring-original-wordmark.svg">
<img align="center" alt="Augusto-POSTGRESQL" height="60" width="60" src="https://raw.githubusercontent.com/devicons/devicon/1119b9f84c0290e0f0b38982099a2bd027a48bf1/icons/postgresql/postgresql-original-wordmark.svg">
<img align="center" alt="Augusto-HTML" height="60" width="60" src="https://raw.githubusercontent.com/devicons/devicon/1119b9f84c0290e0f0b38982099a2bd027a48bf1/icons/html5/html5-plain.svg">
<img align="center" alt="Augusto-JAVASCRIP" height="60" width="60" src="https://raw.githubusercontent.com/devicons/devicon/1119b9f84c0290e0f0b38982099a2bd027a48bf1/icons/javascript/javascript-plain.svg">
<img align="center" alt="Augusto-BOOTSTRAP" height="60" width="60" src="https://raw.githubusercontent.com/devicons/devicon/1119b9f84c0290e0f0b38982099a2bd027a48bf1/icons/bootstrap/bootstrap-plain-wordmark.svg">
<img align="center" alt="Augusto-JQUERY" height="60" width="60" src="https://raw.githubusercontent.com/devicons/devicon/1119b9f84c0290e0f0b38982099a2bd027a48bf1/icons/jquery/jquery-plain.svg">

</div>    

## Teste o projeto 👁‍🗨

Download do projeto para testar em sua máquina: https://github.com/AugustoMello09/Spring-Boot-REST-API-/archive/refs/heads/main.zip

## Entre em contato comigo através dos canais abaixo e desde já, agradeço a atenção. 🤝

<div>

  <a href="mailto:joseaugusto.Mello01@gmail.com" target="_blank"><img src="https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white" target="_blank"></a>
  <a href="https://www.linkedin.com/in/jos%C3%A9-augusto-mello-794a94234" target="_blank"><img src="https://img.shields.io/badge/-LinkedIn-%230077B5?style=for-the-badge&logo=linkedin&logoColor=white" target="_blank"></a>   

  </div>
