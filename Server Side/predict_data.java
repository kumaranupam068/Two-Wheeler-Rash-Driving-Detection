/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Logic.info;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fgfdg
 */
public class predict_data extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String adata=request.getParameter("data");
           
           String ndata="";
           String arr[]=adata.split("\n");
          System.out.println(">>>>>>>>>>>>>>>"+arr.length);
           if(arr.length>=9)
           {
           
           for(int i=0;i<9;i++)
           {
               System.out.println("?????"+arr[i]);
           ndata+=arr[i]+"\n";
           }
           File f1=new File(info.py_path+"Test/1.txt");
           if(!f1.exists())
           {
           f1.createNewFile();
           }
           FileOutputStream fout=new FileOutputStream(f1);
           fout.write(ndata.getBytes());
           fout.close();
           
           
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(predict_data.class.getName()).log(Level.SEVERE, null, ex);
            }
            File file = new File(info.py_path+"output.txt");
            BufferedReader br = new BufferedReader(new FileReader(file)); 
  
                   String st="",data=""; 
                   while ((st = br.readLine()) != null) 
                   {
                   System.out.println(st);
                   data=st;
                   }
                   out.print(data);
                   
           }
                  
           
           
           
           
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
