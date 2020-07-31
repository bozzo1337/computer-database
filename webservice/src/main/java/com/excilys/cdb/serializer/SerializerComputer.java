package com.excilys.cdb.serializer;

import static com.excilys.cdb.config.RestConfig.COMPANIES_ENDPOINT;
import static com.excilys.cdb.config.RestConfig.COMPUTERS_ENDPOINT;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dto.DTOComputer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class SerializerComputer extends StdSerializer<DTOComputer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SerializerComputer.class);
	private static final long serialVersionUID = 1L;

	public SerializerComputer() {
		this(DTOComputer.class);
	}

	protected SerializerComputer(Class<DTOComputer> t) {
		super(t);
	}

	@Override
	public void serialize(DTOComputer computerDTO, JsonGenerator gen, SerializerProvider provider) throws IOException {
		try {
			gen.writeStartObject();
			gen.writeStringField("id", computerDTO.getId());
			gen.writeStringField("name", computerDTO.getName());
			gen.writeStringField("companyId", computerDTO.getCompanyDTO().getId());
			writeLinksDTO(computerDTO, gen);
		} catch (IOException e) {
			LOGGER.error("Error during DTOComputer serialization", e);
		}
	}

	private void writeLinksDTO(DTOComputer computerDTO, JsonGenerator gen) throws IOException {
		gen.writeArrayFieldStart("links");
		gen.writeStartObject();
		gen.writeStringField("href", COMPUTERS_ENDPOINT + "/" + computerDTO.getId());
		gen.writeStringField("rel", "self");
		gen.writeStringField("type", "GET");
		gen.writeEndObject();
		if (!computerDTO.getCompanyDTO().getId().equals("")) {
			gen.writeStartObject();
			gen.writeStringField("href", COMPANIES_ENDPOINT + "/" + computerDTO.getCompanyDTO().getId());
			gen.writeStringField("rel", "company");
			gen.writeStringField("type", "GET");
			gen.writeEndObject();
			gen.writeEndArray();
			gen.writeEndObject();
		}
	}

}
