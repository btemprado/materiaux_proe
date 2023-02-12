package bruno;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;

public class Materiaux_proe
    {

	private JFrame frame;
	private final Action action = new SwingAction();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
	    EventQueue.invokeLater(new Runnable()
	    {
		public void run()
		{
		    try
		    {
			Materiaux_proe window = new Materiaux_proe();
			window.frame.setVisible(true);
		    } catch (Exception e)
		    {
			e.printStackTrace();
		    }
		}
	    });
	}

	/**
	 * Create the application.
	 */
	public Materiaux_proe()
	{
	    initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
	    frame = new JFrame();
	    frame.setBounds(100, 100, 694, 435);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.getContentPane().setLayout(new BorderLayout(0, 0));
	    {
	    	JTextArea textArea = new JTextArea();
	    	frame.getContentPane().add(textArea);
	    }
	    {
	    	JPanel panel = new JPanel();
	    	frame.getContentPane().add(panel, BorderLayout.NORTH);
	    	{
	    		JButton btnStart = new JButton("Start");
	    		btnStart.setAction(action);
	    		panel.add(btnStart);
	    	}
	    }
	}

	private class SwingAction extends AbstractAction {
		/**
	     * 
	     */
	    private static final long serialVersionUID = -5223557314239898254L;
		public SwingAction() {
			putValue(NAME, "Start");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) 
		{
		    
		compile_mat_proe cmp= new compile_mat_proe();
		int res = cmp.compile();
		JOptionPane.showMessageDialog(frame, "write file success : "+ res);		
		}
	}
    }
