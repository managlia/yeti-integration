package com.yeti.core.search.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeti.core.repository.company.CompanyRepository;
import com.yeti.core.repository.contact.ContactRepository;
import com.yeti.model.company.Company;
import com.yeti.model.contact.Contact;
import com.yeti.model.search.CompanyOrContact;

@Service
public class CompanyOrContactService {

	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	
	public TreeSet<CompanyOrContact> searchCompanyOrContact(String term) {
		term = term.toLowerCase();
		LinkedList<String> terms = new LinkedList<String>();
		StringTokenizer st = new StringTokenizer(term);
		while( st.hasMoreTokens() ) {
			terms.add(st.nextToken());
		}
		TreeSet<CompanyOrContact> cocs = new TreeSet<CompanyOrContact>();
		Future<List<Company>> futureCompanyList = companyRepository.searchCompaniesByTerm(terms.getFirst());
		Future<List<Contact>> futureContactList = contactRepository.searchContactsByTerm(terms.getFirst());
		cocs.addAll(this.searchCompanies(terms, futureCompanyList));
		cocs.addAll(this.searchContacts(terms, futureContactList));
		return cocs;
	}

	public TreeSet<CompanyOrContact> searchCompany(String company) {
		String term = company.toLowerCase();
		LinkedList<String> terms = new LinkedList<String>();
		StringTokenizer st = new StringTokenizer(term);
		while( st.hasMoreTokens() ) {
			terms.add(st.nextToken());
		}
		TreeSet<CompanyOrContact> cocs = new TreeSet<CompanyOrContact>();
		Future<List<Company>> futureCompanyList = companyRepository.searchCompaniesByTerm(terms.getFirst());
		cocs.addAll(this.searchCompanies(terms, futureCompanyList));
		return cocs;
	}

	public TreeSet<CompanyOrContact> searchContact(String contact) {
		String term = contact.toLowerCase();
		LinkedList<String> terms = new LinkedList<String>();
		StringTokenizer st = new StringTokenizer(term);
		while( st.hasMoreTokens() ) {
			terms.add(st.nextToken());
		}
		TreeSet<CompanyOrContact> cocs = new TreeSet<CompanyOrContact>();
		Future<List<Contact>> futureContactList = contactRepository.searchContactsByTerm(terms.getFirst());
		cocs.addAll(this.searchContacts(terms, futureContactList));
		return cocs;
	}
	
	public TreeSet<CompanyOrContact> searchCompanies(LinkedList<String> terms, Future<List<Company>> future) {
		TreeSet<CompanyOrContact> cocs = new TreeSet<CompanyOrContact>();

		try {
			List<Company> companies = new ArrayList<Company>();
			while(!future.isDone()) {
				Thread.sleep(50);
			}
			companies = future.get();
			here: for( Company company : companies ) {
				String concatString = company.getName() + ", " + company.getDescription();
				for( String term: terms ) {
					if( concatString.toLowerCase().indexOf(term) == -1 ) {
						continue here;
					}
				}
				CompanyOrContact coc = new CompanyOrContact();
				coc.setMatchTerm(concatString);
				coc.setEntiyType("company");
				coc.setCompanyId(company.getCompanyId());
				cocs.add(coc);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return cocs;
	}
	
	public TreeSet<CompanyOrContact> searchContacts(LinkedList<String> terms, Future<List<Contact>> future) {
		TreeSet<CompanyOrContact> cocs = new TreeSet<CompanyOrContact>();
		try {
			List<Contact> contacts = new ArrayList<Contact>();
			while(!future.isDone()) {
				Thread.sleep(50);
			}
			contacts = future.get();
			here: for( Contact contact : contacts ) {
				String concatString = contact.getFirstName() + " " + contact.getLastName() + ", " + contact.getDescription();
				for( String term: terms ) {
					if( concatString.toLowerCase().indexOf(term) == -1 ) {
						continue here;
					}
				}
				CompanyOrContact coc = new CompanyOrContact();
				coc.setMatchTerm(concatString);
				coc.setEntiyType("contact");
				coc.setContactId(contact.getContactId());
				cocs.add(coc);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return cocs;
	}

	
	
	

}
