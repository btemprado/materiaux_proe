package bruno;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Classe de generation des fichiers mtl PROE 9.0
 *
 * @author Bruno TEMPRADO
 * @version 1.0
 *
 */
public class compile_mat_proe
{
    /**
     * {@value} repertoire de sauvegarde des fichiers mtl
     */
    private static String                   dest    = null;
    /**
     * chemin complet vers le fichier csv base de donnees
     */
    private static String                   src     = null;
    /**
     * {@value} tableau de chaine de caractere des nom des parametres a creer
     */
    private String[]                        param_name_tab;
    /**
     * {@value} tableau de chaine de caractere des types de donnees
     * correspondant aux parametres a creer
     */
    private String[]                        param_type_tab;
    /**
     * {@value} tableau de chaine de caractere des unites correspondant aux
     * parametres a creer
     */
    private String[]                        param_units_tab;
    /**
     * {@value} tableau de chaine de caractere des droits d'acces correspondant
     * aux parametres a creer
     */
    private String[]                        param_access_tab;
    /**
     * {@value} Version du logiciel
     */
    private static String                   version = "1.0";
    /**
     * {@value} Liste de type Map servant a stocker la compilation des fichiers
     * matiere
     */
    private final List<Map<String, String>> data    = new ArrayList<>();

    public static void main(String[] args)
    {
        // create the command line parser
        final CommandLineParser parser = new DefaultParser();
        // create the Options
        final Options options = new Options();
        options.addOption("h", "help", false, "print this message");
        // Ajout option h => help : usage de la commande
        options.addOption("v", "version", false, "display version");
        // Ajout option v => Version : Display Software version
        options.addRequiredOption("src", "src", true, "Csv source file");
        // Ajout option set Data source (Obligatoire) : Variable src
        options.addRequiredOption("dest", "dest", true, "Destination folder");
        // Ajout option set Destination Folder (Obligatoire) : Variable dest
        final HelpFormatter formatter = new HelpFormatter();
        CommandLine         line      = null;
        try
        {
            line = parser.parse(options, args);
        }
        catch (final ParseException e)
        {
            // oops, something went wrong
            final String syntax      = "compile_mat_proe";
            final String usageHeader = "Generation tools for Creo Parametric Material Files";
            final String usageFooter = "Contact B.TEMPRADO if issues";
            System.out.println("\n====");
            System.out.println("HELP");
            System.out.println("====");
            formatter.printHelp(syntax, usageHeader, options, usageFooter);
            System.exit(1);
        }
        if (line.hasOption("h")) // Display Help
        {
            formatter.printHelp("compile_mat_proe --src=xxx.csv --dest=folder...", options);
            System.exit(1);
        }
        if (line.hasOption("src")) // Recup source data
        {
            src = line.getOptionValue("src");
            System.out.println("csv source : " + src);
        }
        if (line.hasOption("dest")) // Recup destination folder
        {
            dest = line.getOptionValue("dest");
            System.out.println("destination folder : " + dest);
        }
        if (line.hasOption("version")) // Display Version
        {
            System.out.println("Version : " + version);
        }
        new compile_mat_proe();
    }

    /**
     * Constructeur de la classe
     *
     */
    public compile_mat_proe()
    {
        compile();
    }

    /**
     * Charge les donnees du fichier materiaux Ensuite les met en forme en n
     * fichiers mtl
     *
     * @return 1 Succes -1 Error
     */
    public int compile()
    {
        if ((dest != null) && (src != null))
        {
            Map<String, String> temp;
            load_database_mat(src);
            for (int x = 0; x < data.size(); x++)
            {
                temp = data.get(x);
                write_mat_file(dest, temp.get("FNAME"), temp);
            }
            return (1);
        }
        else
        {
            return (-1);
        }
    }

    /**
     * Charge la base de donnee materiaux a partir du fichier csv
     *
     * @param AbsolutePath
     *     Chemin complet vers le ficher de donnee csv
     * @return null
     */
    private void load_database_mat(String file_path)
    {
        try
        {
            final List<String> lignes        = FileUtils.readLines(new File(file_path), "UTF-8");
            final String       param_descrip = lignes.get(0);
            final String       param_type    = lignes.get(1);
            final String       param_name    = lignes.get(2);
            final String       param_units   = lignes.get(3);
            final String       param_access  = lignes.get(4);
            param_name_tab   = StringUtils.splitByWholeSeparatorPreserveAllTokens(param_name, ";");
            param_type_tab   = StringUtils.splitByWholeSeparatorPreserveAllTokens(param_type, ";");
            param_units_tab  = StringUtils.splitByWholeSeparatorPreserveAllTokens(param_units, ";");
            param_access_tab = StringUtils.splitByWholeSeparatorPreserveAllTokens(param_access, ";");
            System.out.println("descrip : " + param_descrip);
            System.out.println("type : " + param_type);
            System.out.println("name : " + param_name);
            System.out.println("Units : " + param_units);
            System.out.println("Access : " + param_access);
            // Suppression des 5 premières lignes NON DATA (descrip, type, name,
            // Units, Access)
            lignes.remove(0);
            lignes.remove(0);
            lignes.remove(0);
            lignes.remove(0);
            lignes.remove(0);
            for (int i = 0; i < lignes.size(); i++)
            {
                final String[]            txt_temp = StringUtils.splitByWholeSeparatorPreserveAllTokens(lignes.get(i), ";");
                final Map<String, String> m        = new HashMap<>();
                for (int cp = 0; cp < param_name_tab.length; cp++)
                {
                    m.put(param_name_tab[cp], txt_temp[cp]);
                }
                System.out.println(m);
                data.add(m);
            }
            System.out.println(data);
        }
        catch (final IOException e)
        {
            System.out.println("Erreur : " + e.getMessage() + "\n Fin du programme");
            System.exit(255);
        }
    }

    /**
     * Ecrit le fichier matiere dans le dossier suivant : dest
     *
     * @param AbsolutePath
     *     for destination folder
     * @param fname
     *     Nom du fichier matiere a creer sans extension
     * @param param
     *     Map<String,String> contenant les differents parametre et valeur a
     *     ajouter au fichier mtl
     * @return null
     */
    private void write_mat_file(String path, String fname, Map<String, String> param)
    {
        final StringBuilder str_output  = new StringBuilder();
        String              txt_units   = "";
        final String        file_dest   = path + "\\" + fname + ".mtl";
        final String        entete      = "ND_RelParSet_K01 = { \n\nName = " + StringUtils.upperCase(fname) + "\n\nPARAMETERS = \n\n";
        final String        end_of_file = "}";
        str_output.append(entete);
        for (int cp = 0; cp < param_name_tab.length; cp++)
        {
            str_output.append("{\n");
            str_output.append("  Name = " + param_name_tab[cp] + "\n");
            str_output.append("  Type = " + param_type_tab[cp] + "\n");
            final String valtemp = StringUtils.replaceChars(param.get(param_name_tab[cp]), ',', '.');
            if (!param_units_tab[cp].isEmpty())
            {
                txt_units = " " + param_units_tab[cp];
            }
            else
            {
                txt_units = "";
            }
            if (StringUtils.equals(param_type_tab[cp], "String"))
            {
                str_output.append("  Default = '" + valtemp + txt_units + "'\n");
            }
            else
            {
                str_output.append("  Default = " + valtemp + txt_units + "\n");
            }
            str_output.append("  Access = " + param_access_tab[cp] + "\n");
            str_output.append("}");
            if (cp < (param_name_tab.length - 1))
            {
                str_output.append(",\n");
            }
            else
            {
                str_output.append("\n");
            }
        }
        str_output.append(end_of_file);
        try
        {
            FileUtils.writeStringToFile(new File(file_dest), str_output.toString(), "UTF-8");
        } 
        catch (final IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
