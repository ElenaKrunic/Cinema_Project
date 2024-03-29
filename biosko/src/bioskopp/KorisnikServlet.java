package bioskopp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import bioskop.model.Korisnik;
import bioskop.model.Uloga;
import biosko.DAO.ConnectionManager;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import biosko.DAO.ConnectionManager;
import biosko.DAO.KorisnikDAO;
import bioskop.model.Korisnik;


/**
 * Servlet implementation class KorisnikServlet
 */
public class KorisnikServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response ) throws IOException {
		PrintWriter out = response.getWriter(); 
		String action = request.getParameter("action"); 
		String ID = request.getParameter("korisnikID"); 
		
		if (action != null && request != null) {
			switch(action) {
			//ovde mi je registrationSubmit, ID submita iz HTML-a i onClick iz Registracija.js 
			case "registration" : 
				out.print(registerUser(request));
				break; 
			case "login" :
				out.print(loginUser(request));
				break; 
				
			case "getUserSessionInfo" : 
				out.print(podaciOulogovanomKorisniku(request));
				break;
				
			case "ucitajPodatkeZaMojProfil": 
				out.print(ucitajPodatkeZaMojProfil(request));
				break;
				
			case "obrisiKorisnika": 
				out.print(obrisiKorisnika(request));
				break;
				
			case "sacuvajIzmjene" : 
				out.print(sacuvajIzmjene(request));
				break;
				
			case "ucitajKorisnike":
				out.print(ucitajKorisnike(request));
				break;
				
			case "filtrirajKorisnike": 
				out.print(filtrirajKorisnike(request));
				break;
				
			case "izvjestaj": 
				String datum = request.getParameter("datum"); 
				out.print(uzmiIzvjestaj(datum));
				break;
			}
		}
	}
	
	private JSONObject uzmiIzvjestaj(String datum) {
		JSONObject response = new JSONObject(); 
		response = KorisnikDAO.uzmiIzvjestaj(datum);
		return response;
	}

	//filtrirati korisnike samo po ona 3 parametra iz javascripta 
	private JSONObject filtrirajKorisnike(HttpServletRequest request) {
		JSONObject response = new JSONObject();
		String userName = request.getParameter("userName"); 
		String uloga = request.getParameter("uloga"); 
		String datum = request.getParameter("datum"); 
		
		response = KorisnikDAO.filtrirajKorisnike(userName,uloga,datum);
		
		
		return response;
	}

	private JSONObject ucitajKorisnike(HttpServletRequest request) {
		JSONObject response = new JSONObject(); 
		response = KorisnikDAO.getAllUsers("","",""); 
		return response;
	}

	private JSONObject sacuvajIzmjene(HttpServletRequest request) {
		JSONObject response = new JSONObject(); 
		String id = request.getParameter("idKorisnik"); 
		String novaSifra = request.getParameter("novaSifra"); 
		String novaUloga = request.getParameter("novaUloga"); 
		
		boolean sifra = false; 
		boolean uloga = false; 
		
		uloga = KorisnikDAO.promijeniUlogu(request,id,novaUloga); 
		sifra = KorisnikDAO.promijeniSifru(request,id,novaSifra); 
		
		response.put("promijenjenaSifra", sifra); 
		response.put("promijenjenaUloga", uloga); 
		return response;
	}

	private JSONObject obrisiKorisnika(HttpServletRequest request) {
		JSONObject response = new JSONObject();
		String id = request.getParameter("idKorisnik"); 
		boolean status=true;
		if(id==null) {
			System.out.println("Id korisnika je null, nije doslo sa js-a"); 
		} else {
			status = false; 
			status = KorisnikDAO.obrisiKorisnika(id); 
		}
		response.put("status",status);
		return response;
	}

	private JSONObject ucitajPodatkeZaMojProfil(HttpServletRequest request) {
		JSONObject response = new JSONObject(); 
		String id = request.getParameter("id");
		if(id==null) {
			System.out.println("Nije se ucitao id iz js-a "); 
			
		} else {
			response = KorisnikDAO.ucitajKorisnikObjectById(id);
		}
		//System.out.println("Id korisnika je " + id); 
		//System.out.println("Response koji saljem na js je "  + response); 
//		System.out.println("Ne moze se ucitati korisnik"); 
		return response;
	}

	private JSONObject podaciOulogovanomKorisniku(HttpServletRequest request) {
	return KorisnikDAO.getKorisnikInfo(request); 	
	}

	private JSONObject loginUser(HttpServletRequest request) {
		JSONObject response = KorisnikDAO.login(request); 
		return response;
	}

	//izlazi da bude JSON Object
	private JSONObject registerUser(HttpServletRequest request) {
		JSONObject response = new JSONObject(); 
		response = KorisnikDAO.register(request); 
		return response;
	}
	
	private JSONObject loadAllUsers() {
		JSONObject response = new JSONObject(); 
		response = KorisnikDAO.loadAllUsers("", "", "", "");
		return response; 
	}
}
