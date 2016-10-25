/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author Jelena
 */
public interface IWindowShowMessages {

    public void showMessage(String title, String message);

    public void showErrorMessage(String title, String message);

    public void showQuestionMessage(String title, String message);
}
