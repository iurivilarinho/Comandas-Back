package com.br.comanda.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.br.comanda.models.Documento;

@Service
public class DocumentoService {

	public Documento converterEmDocumento(MultipartFile file) throws IOException {
		if (file != null) {
			return new Documento(file.getName(), file.getContentType(), file.getBytes());
		}
		return null;
	}

	public Set<Documento> converterEmListaDocumento(Set<MultipartFile> file) throws IOException {

		if (file != null) {
			Set<Documento> documentos = new HashSet<>();
			file.forEach(f -> {
				if (!f.isEmpty()) {
					try {
						documentos.add(new Documento(f.getName(), f.getContentType(), f.getBytes()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			return documentos;
		} else {
			return null;
		}

	}
}
