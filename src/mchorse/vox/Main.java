package mchorse.vox;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Main
{
    public static void main(String[] args)
    {
        File file = new File(args[0]);

        if (file.exists() && file.getName().endsWith(".vox"))
        {
            processFile(file);

            System.out.println("Done!");

            return;
        }

        System.err.println("Given file '" + file.getAbsolutePath() + "' isn't a .vox file or doesn't exist.");
    }

    private static void processFile(File file)
    {
        try
        {
            InputStream stream = new FileInputStream(file);
            Vox vox = new VoxReader().read(stream);

            System.out.println("Vox file was read. Moving onto exporting!");

            exportOBJ(vox, file);
            savePalette(vox, file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void exportOBJ(Vox vox, File file)
    {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf("."));

        writeToFile(new File(file.getParentFile(), name + ".mtl"), "# MagicaVoxel @ Ephtracy\n\nnewmtl palette\nillum 1\nKa 0.000 0.000 0.000\nKd 1.000 1.000 1.000\nKs 0.000 0.000 0.000\nmap_Kd " + name + ".png");
        writeToFile(new File(file.getParentFile(), name + ".obj"), new OBJBuilder().buildOBJ(vox, name));
    }

    private static void writeToFile(File file, String content)
    {
        BufferedWriter writer = null;

        try
        {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch (Exception e)
            {}
        }
    }

    private static void savePalette(Vox vox, File file) throws Exception
    {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

        for (int i = 1; i < 256; i++)
        {
            int x = i % 16;
            int y = i / 16;

            image.setRGB(x, y, vox.palette[i]);
        }

        String name = file.getName();
        name = name.substring(0, name.lastIndexOf("."));

        ImageIO.write(image, "png", new File(file.getParentFile(), name + ".png"));

        System.out.println("Saved palette to a PNG file!");
    }
}