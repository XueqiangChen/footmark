package com.xueqiang.footmark.service.impl;

import com.xueqiang.footmark.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	/**
	 * A common paradigm in batch processing is to ingest data, transform it,
	 * and then pipe it out somewhere else. Here, you need to write a simple
	 * transformer that converts the names to uppercase.
	 *
	 * The input and output types need not be the same. In fact, after one
	 * source of data is read, sometimes the application’s data flow needs a
	 * different data type.
	 * @param person
	 * @return
	 * @throws Exception
	 */
	@Override
	public Person process(final Person person) throws Exception {
		final String firstName = person.getFirstName().toUpperCase();
		final String lastName = person.getLastName().toUpperCase();

		final Person transformedPerson = new Person(firstName, lastName);

		log.info("Converting (" + person + ") into (" + transformedPerson + ")");

		return transformedPerson;
	}

}