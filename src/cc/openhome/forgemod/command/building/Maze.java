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
        
        
        void buildWith(ICommandSender sender, Cube cube) {
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
        int ux = Integer.valueOf(args[0]);
        int uy = Integer.valueOf(args[1]);
        int uz = Integer.valueOf(args[2]);
        int rows = Integer.valueOf(args[3]);
        int columns = Integer.valueOf(args[4]);
        int gridWidth = Integer.valueOf(args[5]);
        int wallThickness = Integer.valueOf(args[6]);
        int wallHeight = Integer.valueOf(args[7]);     
        
        buildMaze(sender, 
            ux, uy, uz, 
            rows, columns, 
            gridWidth, wallThickness, wallHeight
        );
        
        /*
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
        */
    }

    private void buildMaze(ICommandSender sender, 
            int ux, int uy, int uz, 
            int rows, int columns, 
            int gridWidth, int wallThickness, int wallHeight) {
        
        Cube cube = new Cube();
        
        Grid[][] grids = initGrids(ux, uy, uz, rows, columns, gridWidth, wallThickness, wallHeight);
        buildGrids(sender, cube, grids);
        
        // The most left and bottom walls
        cube.doCommandWithoutCheckingBlock(
            sender, toCubeArgs(ux, uy, uz, 
                       rows * gridWidth + wallThickness, wallThickness, wallHeight
                    )
        );
        cube.doCommandWithoutCheckingBlock(
            sender, toCubeArgs(ux, uy, uz + gridWidth, 
                        wallThickness, (columns - 1) * gridWidth + wallThickness, wallHeight
                    )
        );
    }
    
    private Grid[][] initGrids(int ux, int uy, int uz, int rows, int columns, int gridWidth, int wallThickness, int wallHeight) {
        Grid[][] mazeGrids = new Grid[rows][columns];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                int gridUserX = i * gridWidth + wallThickness;
                int gridUserZ = j * gridWidth + wallThickness;
                mazeGrids[i][j] = new Grid(
                        ux + gridUserX, uy, uz + gridUserZ, 
                        gridWidth, wallThickness, wallHeight
                );
            }
        }        
        return mazeGrids;
    }
    
    private void buildGrids(ICommandSender sender, Cube cube, Grid[][] mazeGrids) {
        for(Grid[] rowGrids : mazeGrids) {
            for(Grid grid : rowGrids) {
                grid.buildWith(sender, cube);
            }
        }
    }    
/*
    private void buildMaze(ICommandSender sender, 
            int ux, int uy, int uz, 
            int rows, int columns, int gridWidth,
            int wallThickness, int wallHeight, 
            WallType[][] maze) {
        
        Cube cube = new Cube();
        // maze
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                int gridUserX = i * gridWidth + wallThickness;
                int gridUserZ = j * gridWidth + wallThickness;
                
                Grid grid = new Grid(ux + gridUserX, uy, uz + gridUserZ, gridWidth, wallThickness, wallHeight);
                grid.wallType = maze[rows - i - 1][j];
                grid.buildWith(sender, cube);
            }
        }
        
       // maze sides
       cube.doCommandWithoutCheckingBlock(sender, toCubeArgs(ux, uy, uz, rows * gridWidth + wallThickness, wallThickness, wallHeight));
       cube.doCommandWithoutCheckingBlock(sender, toCubeArgs(ux, uy, uz + gridWidth, wallThickness, (columns - 1) * gridWidth + wallThickness, wallHeight));
    }
    */
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
