package br.com.evolucao.evolucaoFisica.controller;

import br.com.evolucao.evolucaoFisica.dto.OnboardingRequest;
import br.com.evolucao.evolucaoFisica.dto.OnboardingResponse;
import br.com.evolucao.evolucaoFisica.service.OnboardingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @PostMapping
    public OnboardingResponse concluir(@PathVariable Long usuarioId, @Valid @RequestBody OnboardingRequest request) {
        return onboardingService.concluir(usuarioId, request);
    }
}
