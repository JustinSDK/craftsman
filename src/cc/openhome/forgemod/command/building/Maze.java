package cc.openhome.forgemod.command.building;

import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.drawing.Cube;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class Maze implements DefaultCommand {
    private enum WallType {
        NONE, UP, RIGHT, UP_RIGHT,
    }
    
    private class Grid {
        final int userX;
        final int userY;
        final int userZ;
        
        final int width;
        final int wallThickness;
        final int wallHeight;
        
        WallType wallType = WallType.UP_RIGHT;
        
        public Grid(int userX, int userY, int userZ, int width, int wallThickness, int wallHeight) {
            this.userX = userX;
            this.userY = userY;
            this.userZ = userZ;
            this.width = width;
            this.wallThickness = wallThickness;
            this.wallHeight = wallHeight;
        }        
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + userX;
            result = prime * result + userY;
            result = prime * result + userZ;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Grid other = (Grid) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (userX != other.userX)
                return false;
            if (userY != other.userY)
                return false;
            if (userZ != other.userZ)
                return false;
            return true;
        }

        private Maze getOuterType() {
            return Maze.this;
        }

        void buildWith(ICommandSender sender, Cube cube) {
            // <ux> <uy> <uz> <rows> <columns> <layers>
            
            int offset = this.width - this.wallThickness;
            
            String[] upWallArgs = toCubeArgs(userX + offset, userY, userZ, wallThickness, width, wallHeight);
            String[] rightWallArgs = toCubeArgs(userX, userY, userZ + offset, width, wallThickness, wallHeight);
            
            if(wallType == WallType.UP || wallType == WallType.UP_RIGHT) {
                cube.doCommandWithoutCheckingBlock(sender, upWallArgs);
            }
            if(wallType == WallType.RIGHT || wallType == WallType.UP_RIGHT)
                cube.doCommandWithoutCheckingBlock(sender, rightWallArgs); {
            }
        }
        

    }

    
    @Override
    public String getName() {
        return "maze";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return String.format("/%s <ux> <uy> <uz> <rows> <columns> <grid_width> <wall_thickness> <wall_height>", getName());
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
       /* int ux = Integer.valueOf(args[0]);
        int uy = Integer.valueOf(args[1]);
        int uz = Integer.valueOf(args[2]);
        int w = Integer.valueOf(args[3]);
        int t = Integer.valueOf(args[4]);
        int h = Integer.valueOf(args[5]);
        
        Cube cube = new Cube();
        
        Grid grid = new Grid(ux, uy, uz, w, t, h);
        
        grid.buildWith(sender, cube);*/
        
        int ux = Integer.valueOf(args[0]);
        int uy = Integer.valueOf(args[1]);
        int uz = Integer.valueOf(args[2]);
        int rows = 4;    // Integer.valueOf(args[3]);
        int columns = 4; // Integer.valueOf(args[4]);
        int gridWidth = Integer.valueOf(args[5]);
        int wallThickness = Integer.valueOf(args[6]);
        int wallHeight = Integer.valueOf(args[7]);     
        
        WallType[][] maze = {
            {WallType.UP, WallType.UP, WallType.UP, WallType.UP},
            {WallType.RIGHT, WallType.UP_RIGHT, WallType.UP, WallType.RIGHT},
            {WallType.NONE, WallType.RIGHT, WallType.RIGHT, WallType.RIGHT},
            {WallType.UP, WallType.UP, WallType.RIGHT, WallType.RIGHT}  
        };
        
        buildMaze(sender, 
            ux, uy, uz, rows, columns, 
            gridWidth, wallThickness, wallHeight, 
            maze
        );
    }

    private void buildMaze(ICommandSender sender, 
            int ux, int uy, int uz, 
            int rows, int columns, int gridWidth,
            int wallThickness, int wallHeight, 
            WallType[][] maze) {
        
        Cube cube = new Cube();
        // maze
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                int gridUserX = row * gridWidth + wallThickness;
                int gridUserZ = column * gridWidth + wallThickness;
                
                Grid grid = new Grid(ux + gridUserX, uy, uz + gridUserZ, gridWidth, wallThickness, wallHeight);
                grid.wallType = maze[rows - row - 1][column];
                grid.buildWith(sender, cube);
            }
        }
        
       // maze sides
       cube.doCommandWithoutCheckingBlock(sender, toCubeArgs(ux, uy, uz, rows * gridWidth + wallThickness, wallThickness, wallHeight));
       cube.doCommandWithoutCheckingBlock(sender, toCubeArgs(ux, uy, uz + gridWidth, wallThickness, (columns - 1) * gridWidth + wallThickness, wallHeight));
    }
    
    private String[] toCubeArgs(int ux, int uy, int uz,  int rows, int columns, int layers) {
        return new String[ ]{
            String.valueOf(ux),
            String.valueOf(uy),
            String.valueOf(uz),
            String.valueOf(rows),
            String.valueOf(columns),
            String.valueOf(layers)
        };
    }
}
