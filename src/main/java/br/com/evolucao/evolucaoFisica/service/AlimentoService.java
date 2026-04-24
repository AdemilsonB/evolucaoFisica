package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.AlimentoRequest;
import br.com.evolucao.evolucaoFisica.dto.AlimentoResponse;
import br.com.evolucao.evolucaoFisica.entity.Alimento;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.AlimentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AlimentoService {

    private final AlimentoRepository alimentoRepository;

    public AlimentoService(AlimentoRepository alimentoRepository) {
        this.alimentoRepository = alimentoRepository;
    }

    @Transactional
    public AlimentoResponse criar(AlimentoRequest request) {
        Alimento alimento = new Alimento();
        preencher(alimento, request);
        return toResponse(alimentoRepository.save(alimento));
    }

    public List<AlimentoResponse> listar() {
        return alimentoRepository.findAllByOrderByNomeAsc().stream().map(this::toResponse).toList();
    }

    public AlimentoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public AlimentoResponse atualizar(Long id, AlimentoRequest request) {
        Alimento alimento = buscarEntidade(id);
        preencher(alimento, request);
        return toResponse(alimentoRepository.save(alimento));
    }

    @Transactional
    public void excluir(Long id) {
        alimentoRepository.delete(buscarEntidade(id));
    }

    public Alimento buscarEntidade(Long id) {
        return alimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alimento nao encontrado."));
    }

    private void preencher(Alimento alimento, AlimentoRequest request) {
        alimento.setNome(request.nome());
        alimento.setCalorias(request.calorias());
        alimento.setProteina(request.proteina());
        alimento.setCarboidrato(request.carboidrato());
        alimento.setGordura(request.gordura());
        alimento.setAcucares(request.acucares());
    }

    private AlimentoResponse toResponse(Alimento alimento) {
        return new AlimentoResponse(
                alimento.getId(),
                alimento.getNome(),
                alimento.getCalorias(),
                alimento.getProteina(),
                alimento.getCarboidrato(),
                alimento.getGordura(),
                alimento.getAcucares()
        );
    }
}
