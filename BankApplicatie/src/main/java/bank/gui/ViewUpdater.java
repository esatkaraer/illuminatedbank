package bank.gui;

import javafx.scene.control.Label;

import javax.swing.JLabel;

import bank.gui.view.ViewController;

public class ViewUpdater
{
	public ViewUpdater(Label balanceInfoLabel,String saldo)
	{
		balanceInfoLabel.setText("Uw saldo bedraagt : € " + saldo);
	}
}