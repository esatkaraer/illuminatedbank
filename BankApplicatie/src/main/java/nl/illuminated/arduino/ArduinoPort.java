package nl.illuminated.arduino;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import purejavacomm.CommPortIdentifier;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;
import purejavacomm.SerialPort;

public class ArduinoPort
{

    private static SerialPort serialPort;
    private BlockingQueue rxQueue;
    private boolean keepRunning;

    public BlockingQueue getRxQueue()
    {
        return rxQueue;
    }

    public void setRxQueue(BlockingQueue rxQueue)
    {
        this.rxQueue = rxQueue;
    }
    private BufferedReader reader;
    private final SerialPortReader readerThread;
    private final Object arduinoLock;
    private int rowsReceived;
    
    private SerialPort initSerialPort(String port, int baudrate) throws NoSuchPortException, PortInUseException
    {
        CommPortIdentifier identifier = CommPortIdentifier.getPortIdentifier(port);
        SerialPort mySerialPort = (SerialPort) identifier.open("Arduino", baudrate);
        return mySerialPort;
    }
    public void RequestStop()
    {
        keepRunning = false;
    }

    public ArduinoPort(String port, int baudrate) throws NoSuchPortException, PortInUseException
    {
        rxQueue = new LinkedBlockingQueue<>();
        keepRunning = true;
        arduinoLock = new Object();
        this.serialPort = initSerialPort(port, baudrate);
        try
        {
            reader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
        }
        catch (IOException ex)
        {
            Logger.getLogger(ArduinoPort.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.readerThread = new SerialPortReader(reader);
        this.readerThread.setDaemon(false);       
    }

    public void start()
    {
        System.out.println("Arduino thread started...");
        readerThread.start();
        keepRunning = true;
        //handleReceivedPackages();
    }

    public void handleReceivedPackages()
    {
        String row;
        while (keepRunning)
        {
            try
            {
                row = (String)rxQueue.take();
                System.out.println(row);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(ArduinoPort.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void SerialPortWriter()
    {
    	String resetString = "reset";
    	try {
			serialPort.getOutputStream().write(resetString.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    private class SerialPortReader extends Thread
    {

        private final BufferedReader myReader;

        public SerialPortReader(BufferedReader reader)
        {
            this.myReader = reader;
        }

        public void run()
        {
            while (keepRunning)
            {
                try
                {
                    String line = myReader.readLine();
                    rxQueue.put(line);
                }
                catch (IOException | InterruptedException ex)
                {
                    Logger.getLogger(ArduinoPort.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
