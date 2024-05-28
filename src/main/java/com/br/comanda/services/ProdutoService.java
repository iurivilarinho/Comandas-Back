package com.br.comanda.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.br.comanda.models.Produto;
import com.br.comanda.repository.ProdutoRepository;
import com.br.comanda.specification.ProdutoSpecification;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private DocumentoService documentoService;

	@Transactional(readOnly = true)
	public Produto buscarProdutoPorId(Long id) {
		return produtoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Produto não encontrado para ID:" + id));
	}

	@Transactional(readOnly = true)
	public List<Produto> listar(String search) {
		if (search != null) {
			Specification<Produto> spec = ProdutoSpecification.searchAllFields(search);
			return produtoRepository.findAll(spec);
		} else {
			return produtoRepository.findAll();
		}
	}

	@Transactional
	public void cadastrarImagemProduto(Long id, MultipartFile file) throws IOException {
		Produto produto = buscarProdutoPorId(id);
		produto.setImagem(documentoService.converterEmDocumento(file));
	}

}
