package mchorse.vox;

import java.util.ArrayList;
import java.util.List;

public class OBJBuilder
{
    public String content = "";
    public List<Vertex> vertices = new ArrayList<Vertex>();
    public List<Integer> palette = new ArrayList<Integer>();

    public String buildOBJ(Vox vox, String name)
    {
        this.vertices.clear();
        this.palette.clear();

        this.content = "# MagicaVoxel @ Ephtracy\n\n# material\nmtllib " + name + ".mtl\nusemtl palette\n\n# normals\nvn -1 0 0\nvn 1 0 0\nvn 0 0 1\nvn 0 0 -1\nvn 0 -1 0\nvn 0 1 0\n";
        this.content += "\n# palette\n";

        for (int x = 0; x < vox.x; x++)
        {
            for (int y = 0; y < vox.y; y++)
            {
                for (int z = 0; z < vox.z; z++)
                {
                    int voxel = vox.voxels[vox.toIndex(x, y, z)];

                    if (voxel != 0)
                    {
                        this.buildVertex(x, y, z, voxel, vox);

                        if (this.palette.indexOf(voxel) == -1)
                        {
                            this.palette.add(voxel);
                        }
                    }
                }
            }
        }

        for (Integer in : this.palette)
        {
            this.content += "vt " + ((in - 1 + 0.5F) / 256) + " 0.5\n";
        }

        this.content += "\n";

        System.out.println(this.vertices.size());

        for (Vertex v : this.vertices)
        {
            this.content += "v " + (v.x - vox.x / 2F) + " " + v.y + " " + (v.z - vox.z / 2F) + "\n";
        }

        this.content += "\n# faces\n";

        for (int i = 0, c = this.vertices.size() / 4; i < c; i++)
        {
            Vertex v1 = this.vertices.get(i * 4);
            Vertex v2 = this.vertices.get(i * 4 + 1);
            Vertex v3 = this.vertices.get(i * 4 + 2);
            Vertex v4 = this.vertices.get(i * 4 + 3);

            String face = "f ";
            int index = this.palette.indexOf((int) v1.color) + 1;

            face += (i * 4 + 1) + "/" + index + "/" + (v1.normal + 1) + " ";
            face += (i * 4 + 2) + "/" + index + "/" + (v2.normal + 1) + " ";
            face += (i * 4 + 3) + "/" + index + "/" + (v3.normal + 1) + " ";
            face += (i * 4 + 4) + "/" + index + "/" + (v4.normal + 1);

            this.content += face + "\n";
        }

        return this.content;
    }

    private void buildVertex(int x, int y, int z, int voxel, Vox vox)
    {
        boolean top = vox.has(x, y + 1, z);
        boolean bottom = vox.has(x, y - 1, z);
        boolean left = vox.has(x + 1, y, z);
        boolean right = vox.has(x - 1, y, z);
        boolean front = vox.has(x, y, z + 1);
        boolean back = vox.has(x, y, z - 1);

        if (!top)
        {
            this.vertices.add(new Vertex(x, y + 1, z + 1, voxel, 5));
            this.vertices.add(new Vertex(x + 1, y + 1, z + 1, voxel, 5));
            this.vertices.add(new Vertex(x + 1, y + 1, z, voxel, 5));
            this.vertices.add(new Vertex(x, y + 1, z, voxel, 5));
        }

        if (!bottom)
        {
            this.vertices.add(new Vertex(x + 1, y, z + 1, voxel, 4));
            this.vertices.add(new Vertex(x, y, z + 1, voxel, 4));
            this.vertices.add(new Vertex(x, y, z, voxel, 4));
            this.vertices.add(new Vertex(x + 1, y, z, voxel, 4));
        }

        if (!left)
        {
            this.vertices.add(new Vertex(x + 1, y + 1, z, voxel, 1));
            this.vertices.add(new Vertex(x + 1, y + 1, z + 1, voxel, 1));
            this.vertices.add(new Vertex(x + 1, y, z + 1, voxel, 1));
            this.vertices.add(new Vertex(x + 1, y, z, voxel, 1));
        }

        if (!right)
        {
            this.vertices.add(new Vertex(x, y + 1, z + 1, voxel, 0));
            this.vertices.add(new Vertex(x, y + 1, z, voxel, 0));
            this.vertices.add(new Vertex(x, y, z, voxel, 0));
            this.vertices.add(new Vertex(x, y, z + 1, voxel, 0));
        }

        if (!front)
        {
            this.vertices.add(new Vertex(x + 1, y + 1, z + 1, voxel, 2));
            this.vertices.add(new Vertex(x, y + 1, z + 1, voxel, 2));
            this.vertices.add(new Vertex(x, y, z + 1, voxel, 2));
            this.vertices.add(new Vertex(x + 1, y, z + 1, voxel, 2));
        }

        if (!back)
        {
            this.vertices.add(new Vertex(x, y + 1, z, voxel, 3));
            this.vertices.add(new Vertex(x + 1, y + 1, z, voxel, 3));
            this.vertices.add(new Vertex(x + 1, y, z, voxel, 3));
            this.vertices.add(new Vertex(x, y, z, voxel, 3));
        }
    }

    public static class Vertex
    {
        public int x;
        public int y;
        public int z;
        public int color;
        public int normal;

        public Vertex(int x, int y, int z, int color, int normal)
        {
            this.x = x;
            this.y = y;
            this.z = z;
            this.color = color;
            this.normal = normal;
        }
    }
}