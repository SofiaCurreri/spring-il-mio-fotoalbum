package org.java.lessons.springilmiofotoalbum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {
    @GetMapping("/logout")
    public String logout() {
        // Implementa qui la logica per il logout se necessario
        // Esempio: Invalida la sessione o effettua altre operazioni di logout

        // Reindirizza l'utente alla pagina homepage.html nel frontend
        return "redirect:homepage";
    }
}

