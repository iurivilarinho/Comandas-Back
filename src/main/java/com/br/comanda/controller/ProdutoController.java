package com.br.comanda.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.br.comanda.models.Produto;
import com.br.comanda.services.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;

	@GetMapping
	public ResponseEntity<List<Produto>> listar(@RequestParam(required = false) String search) {
		return ResponseEntity.ok(produtoService.listar(search));
	}

	@PostMapping(value = "/{id}", consumes = { "multipart/form-data" })
	public ResponseEntity<?> cadatrarImagemProduto(@PathVariable Long id, @RequestPart MultipartFile file)
			throws IOException {
		produtoService.cadastrarImagemProduto(id, file);
		return ResponseEntity.ok().build();
	}
}
