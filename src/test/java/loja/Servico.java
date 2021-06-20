package loja;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Servico {

    public String tokenGeral;    // Variável para receber o token
    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test                           // Executa os servicos na ordem (inclusão e consulta)
    public void ordemDaExecucao() throws IOException {
        incluirPet();
        consultarPet();
        alterarPet();
        excluirPet();
    }
    // Create/Incluir/POST

    @Test
    public void incluirPet() throws IOException {
        // Ler o conteúdo do arquivo pet.json
        String jsonBody = lerJson("data/pet.json");

        given()                              // Dado que
            .contentType("application/json") // Tipo de conteúdo da requisição
                                            // "text/xml" para webservices comuns
                                            // "application/json" para APIs REST
            .log().all()                    // Gerar um log completo da requisição
            .body(jsonBody)                 //  Conteúdo do corpo da requisição
        .when()                             // Quando
            .post("https://petstore.swagger.io/v2/pet") // Operação e endpoint
        .then()                             // Então
            .log().all()                    // Gerar um log completo da resposta
            .statusCode(200)                // Validou o código de status da requisição como 200
            //.body("code", is(200))          // Valida o code como 200
            .body("id", is(4200))           // Validou a tag id com o conteúdo esperado
            .body("name", is("Snoopy"))     // Validou a tag nome como Snoopy
            .body("tags.name", contains("adotar")) //Validou a tag name, filha da tag Tags
        ;
        System.out.print("Executou o serviço");

    }

    // Reach or Research / Consultar / Get
    @Test
    public void consultarPet(){
        String petId = "4200";

    given()                                   // Dado que
        .contentType("application/json")                    // Tipo de conteúdo da requisição
        .log().all()                                       // Mostrar tudo que foi enviado
    .when()                                   // Quando
        .get("https://petstore.swagger.io/v2/pet/" + petId) // Consulta pelo petId
    .then()                                   // Entao
        .log().all()                                        // Mostrar tudo que foi recebido
        .statusCode(200)                                    // Valida que a operação foi realizada
        .body("name", is("Snoopy"))                         // Valida o nome do pet
        .body("category.name", is("dog"))                   // Valida a espécie
        ;
    }

    @Test
    public void alterarPet() throws IOException {

        String jsonBody = lerJson("data/petput.json");

        given()                            // Dado que
            .contentType("application/json") // Tipo de conteúdo da requisição
            // "text/xml" para webservices comuns
            // "application/json" para APIs REST
            .log().all()                    // Gerar um log completo da requisição
            .body(jsonBody)                 //  Conteúdo do corpo da requisição
        .when()                    // Quando
             .put("https://petstore.swagger.io/v2/pet/")
        .then()
             .log().all()
             .statusCode(200)
             .body("name", is("Snoopy"))
             .body("status", is("adotado"))
             ;
    }

    // Delete / Excluir / Delete

    @Test
    public void excluirPet(){
        String petId = "4200";

        given()                                   // Dado que
                .contentType("application/json")                    // Tipo de conteúdo da requisição
                .log().all()                                       // Mostrar tudo que foi enviado
        .when()                                   // Quando
                .delete("https://petstore.swagger.io/v2/pet/" + petId) // Consulta pelo petId
        .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("message", is(petId))
        ;
    }

    // Login
    @Test
    public void loginUser(){
        // public String loginUser(){

        String token =
        given()                                   // Dado que
                .contentType("application/json")                    // Tipo de conteúdo da requisição
                .log().all()                                       // Mostrar tudo que foi enviado
        .when()
                .get("https://petstore.swagger.io/v2/user/login?username=Drika&password=testeapi")
        .then()
                .log().all()
                .statusCode(200)
                .body("message", containsString("logged in user session:"))
                .extract()
                .path("message")
        ;
        tokenGeral = token.substring(23);   // separa o token da frase
        System.out.println("O token válido " +token.substring(23));
        //return tokenGeral;
    }

    @Test
    public void sempreMetodo() {
        loginUser2();
    }

    public String loginUser2(){

        String token =
                given()                                   // Dado que
                        .contentType("application/json")                    // Tipo de conteúdo da requisição
                        .log().all()                                       // Mostrar tudo que foi enviado
                        .when()
                        .get("https://petstore.swagger.io/v2/user/login?username=Drika&password=testeapi")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("message", containsString("logged in user session:"))
                        .extract()
                        .path("message")
                ;
        String tokenGeral2 = token.substring(23);   // separa o token da frase
        System.out.println("O token válido " + tokenGeral2);
        return tokenGeral2;

    }

}
