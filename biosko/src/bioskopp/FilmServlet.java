package bioskopp;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import biosko.DAO.FilmDAO;
import biosko.DAO.KorisnikDAO;
import bioskop.model.Film;
import bioskop.model.Korisnik;
import bioskop.model.Uloga;
import bioskop.model.Zanr;

/**
 * Servlet implementation class FilmServlet
 */
public class FilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ulogovanKorisnikIme = (String) request.getSession().getAttribute("ulogovanKorisnikIme");
		if(ulogovanKorisnikIme == null) {
			request.getRequestDispatcher("./OdjavaServlet").forward(request, response);
			return;
		}
		try {
			Korisnik ulogovaniKorisnik = KorisnikDAO.get(ulogovanKorisnikIme);
			if(ulogovaniKorisnik == null) {
				request.getRequestDispatcher("/.OdjavaServlet").forward(request, response);
				return;
			}
			String id = request.getParameter("id");
			Film film = FilmDAO.get(id);
			
			Map<String,Object> data = new LinkedHashMap<>(); 
			data.put("film", film); 
			data.put("ulogovanKorisnikUloga", ulogovaniKorisnik.getUloga());
			
				request.setAttribute("data", data);
				request.getRequestDispatcher("./UspjesnoServlet").forward(request, response);
	} catch(Exception ex) {ex.printStackTrace();}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ulogovanKorisnikIme = (String) request.getSession().getAttribute("ulogovanKorisnikIme");
		if(ulogovanKorisnikIme == null) {
			request.getRequestDispatcher("./OdjavaServlet").forward(request, response);
			return;
		}
		try {
			Korisnik ulogovaniKorisnik = KorisnikDAO.get(ulogovanKorisnikIme);
			if(ulogovaniKorisnik == null) {
				request.getRequestDispatcher("./OdjavaServlet").forward(request, response);
				return;
			}
			if(ulogovaniKorisnik.getUloga() != Uloga.Administrator) {
				request.getRequestDispatcher("./NeovlascenoServlet").forward(request, response);
				return;
			}
			
			String action = request.getParameter("action");
			switch(action) {
			case "add" : {
				String naziv = request.getParameter("naziv"); 
				naziv = (!"".equals(naziv)? naziv : "<empty>");
				
				Zanr zanr = Zanr.valueOf(request.getParameter("zanr"));
				
				String trajanje = request.getParameter("trajanje"); 
				trajanje = (!"".equals(trajanje)? trajanje: "<empty>");
				
				String distributer = request.getParameter("distributer"); 
				distributer = (!"".equals(distributer)? distributer : "<empty>");
				
				String zemljaPorijekla = request.getParameter("zemljaPorijekla");
				zemljaPorijekla = (!"".equals(zemljaPorijekla)? zemljaPorijekla : "<empty>");
				
				int godinaProizvodnje = Integer.parseInt(request.getParameter("godinaProizvodnje"));
				godinaProizvodnje = (int) (godinaProizvodnje > 0? godinaProizvodnje: 2020);
				
				Film film = new Film ("",naziv,zanr,trajanje,distributer,zemljaPorijekla,godinaProizvodnje);
				FilmDAO.add(film);
				break;								
			}
			case "update" : {
				String id = request.getParameter("id"); 
				Film film = FilmDAO.get(id);
				
				String naziv = request.getParameter("naziv"); 
				naziv = (!"".equals(naziv)? naziv : film.getNaziv());
				
				Zanr zanr = Zanr.valueOf(request.getParameter("zanr"));
				
				String trajanje = request.getParameter("trajanje"); 
				trajanje = (!"".equals(trajanje)? trajanje: film.getTrajanje());
				
				String distributer = request.getParameter("distributer"); 
				distributer = (!"".equals(distributer)? distributer : film.getDistributer());
				
				String zemljaPorijekla = request.getParameter("zemljaPorijekla");
				zemljaPorijekla = (!"".equals(zemljaPorijekla)? zemljaPorijekla : film.getZemljaPorijekla());
				
				int godinaProizvodnje = Integer.parseInt(request.getParameter("godinaProizvodnje"));
				godinaProizvodnje = (int) (godinaProizvodnje > 0? godinaProizvodnje: film.getGodinaProizvodnje());
				
				film.setNaziv(naziv);
				film.setZanr(zanr);
				film.setTrajanje(trajanje);
				film.setDistributer(distributer);
				film.setZemljaPorijekla(zemljaPorijekla);
				film.setGodinaProizvodnje(godinaProizvodnje);
				FilmDAO.update(film);
				break;
			} 
			case "delete" : {
				String id = request.getParameter("id"); 
				FilmDAO.delete(id);
				break;
			}
		} 
		request.getRequestDispatcher("./UspjesnoServlet").forward(request, response);
	} catch(Exception e) {e.printStackTrace();
	request.getRequestDispatcher(".NeuspjesnoServlet").forward(request, response);
	}
}

}