package biosko.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bioskop.model.Sala;
import bioskop.model.TipProjekcije;
public class SalaDAO {

	public static Sala getSalaObjectById(int salaId) {
		Connection conn = ConnectionManager.getConnection(); 
		PreparedStatement prep = null; 
		ResultSet rs = null; 
		Sala sala = null; 
		
		try {
			String query = "SELECT ID,Naziv,ID_Tipova_Projekcija FROM Sale WHERE ID = ?";
			
			prep = conn.prepareStatement(query); 
			prep.setString(1, String.valueOf(salaId));
			rs = prep.executeQuery(); 
			
			if(rs.next()) {
				int index = 1; 
				int id = Integer.valueOf(rs.getString(index++)); 
				String naziv = rs.getString(index++); 
				String[] idProjekcija = rs.getString(index++).split(";");
				ArrayList<TipProjekcije> listaTipovaProj = new ArrayList<TipProjekcije>();
				for(String idProjekcije : idProjekcija) {
					TipProjekcije tip = TipProjekcijeDAO.getTipProjekcijeObjectById(Integer.valueOf(idProjekcije));
					listaTipovaProj.add(tip);
				}
				
				sala = new Sala(id,naziv,listaTipovaProj); 
			} else {
				
			}
		} catch(Exception e) {
			System.out.println("Ne postoji sala"); 
		}
		
		finally {
			try {prep.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rs.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
		}
		
		return sala;
	}
	/*

	public static Sala getSalaObjectById(int salaId) {
		Connection conn = ConnectionManager.getConnection(); 
		PreparedStatement prep = null; 
		ResultSet rs = null;
		
		Sala sala = null; 

		try {
			String query = "SELECT ID,Naziv,ID_Tipova_Projekcija FROM Sale WHERE ID = ?";
			
			prep= conn.prepareStatement(query); 
			prep.setString(1, String.valueOf(salaId));
			
			rs= prep.executeQuery(); 
			
			if(rs.next()) {
				int index = 1; 
				int ID = Integer.valueOf(rs.getString(index++)); 
				String Naziv = rs.getString(index++); 
				String[] tipoviProjekcijaID = rs.getString(index++).split(";");
				ArrayList<TipProjekcije> listaTipovaProjekcije =  new ArrayList<TipProjekcije>();
				
				for(String string : tipoviProjekcijaID) {
					TipProjekcije tip = TipProjekcijeDAO.getTipProjekcijeObject(Integer.valueOf(string)); 
					if(tip != null) {
						listaTipovaProjekcije.add(tip); 
					} else {
						System.out.println("Tip projekcije sa tim IDjem ne postoji"); 
					}
				}
				sala = new Sala(ID,Naziv, listaTipovaProjekcije);
			} 
			else {
				System.out.println("Nije vracena sala sa projekcijama"); 
			}
		} catch(Exception e) {
			System.out.println("Ne moze se pronaci sala sa tim id-jem"); 
		}
		
		 finally {
				try {prep.close();} catch (Exception ex1) {ex1.printStackTrace();}
				try {rs.close();} catch (Exception ex1) {ex1.printStackTrace();}
				try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
		 }
		
		return sala;
	}
*/
}
