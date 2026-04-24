package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.EvolucaoFisicaRequest;
import br.com.evolucao.evolucaoFisica.dto.EvolucaoFisicaResponse;
import br.com.evolucao.evolucaoFisica.entity.EvolucaoFisica;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.EvolucaoFisicaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EvolucaoFisicaService {

    private final EvolucaoFisicaRepository evolucaoFisicaRepository;
    private final UsuarioService usuarioService;

    public EvolucaoFisicaService(EvolucaoFisicaRepository evolucaoFisicaRepository, UsuarioService usuarioService) {
        this.evolucaoFisicaRepository = evolucaoFisicaRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public EvolucaoFisicaResponse registrar(EvolucaoFisicaRequest request) {
        EvolucaoFisica evolucaoFisica = new EvolucaoFisica();
        preencher(evolucaoFisica, request);
        return toResponse(evolucaoFisicaRepository.save(evolucaoFisica));
    }

    public List<EvolucaoFisicaResponse> listarHistorico(Long usuarioId) {
        return evolucaoFisicaRepository.findAllByUsuarioIdOrderByDataRegistroDesc(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public EvolucaoFisicaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public EvolucaoFisica buscarEntidadeInterna(Long id) {
        return buscarEntidade(id);
    }

    @Transactional
    public EvolucaoFisicaResponse atualizar(Long id, EvolucaoFisicaRequest request) {
        EvolucaoFisica evolucaoFisica = buscarEntidade(id);
        preencher(evolucaoFisica, request);
        return toResponse(evolucaoFisicaRepository.save(evolucaoFisica));
    }

    @Transactional
    public void excluir(Long id) {
        evolucaoFisicaRepository.delete(buscarEntidade(id));
    }

    private EvolucaoFisica buscarEntidade(Long id) {
        return evolucaoFisicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de evolucao nao encontrado."));
    }

    private void preencher(EvolucaoFisica evolucaoFisica, EvolucaoFisicaRequest request) {
        evolucaoFisica.setUsuario(usuarioService.buscarEntidade(request.usuarioId()));
        evolucaoFisica.setPeso(request.peso());
        evolucaoFisica.setPercentualGordura(request.percentualGordura());
        evolucaoFisica.setMassaMagra(request.massaMagra());
        evolucaoFisica.setDataRegistro(request.dataRegistro());
    }

    private EvolucaoFisicaResponse toResponse(EvolucaoFisica evolucaoFisica) {
        return new EvolucaoFisicaResponse(
                evolucaoFisica.getId(),
                evolucaoFisica.getUsuario().getId(),
                evolucaoFisica.getPeso(),
                evolucaoFisica.getPercentualGordura(),
                evolucaoFisica.getMassaMagra(),
                evolucaoFisica.getDataRegistro()
        );
    }
}
