package cc.openhome.forgemod.command.building;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import cc.openhome.forgemod.command.Commons;
import cc.openhome.forgemod.command.DefaultCommand;
import cc.openhome.forgemod.command.FstDimension;
import cc.openhome.forgemod.command.FstPos;
import cc.openhome.forgemod.command.drawing.Cube;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class Maze implements DefaultCommand {
    private static enum WallType {
        NONE, UP, RIGHT, UP_RIGHT,
    }
    
    private static enum Dir {
        UP, DOWN, LEFT, RIGHT
    }
    
    private static class Grid {
        final FstPos origin;
        
        final int width;
        final int wallThickness;
        final int wallHeight;
        
        WallType wallType = WallType.UP_RIGHT;
        
        boolean visited;
        
        Grid(FstPos origin, int width, int wallThickness, int wallHeight) {
            this.origin = origin;
            this.width = width;
            this.wallThickness = wallThickness;
            this.wallHeight = wallHeight;
        }        
    }
    
    private static class GridsCreator {
        final Grid[][] grids;
        
        GridsCreator(FstPos origin, 
                FstDimension mazeDimension, 
                int gridWidth, int wallThickness) {
            this.grids = initGrids(origin, mazeDimension, gridWidth, wallThickness);
            visit(mazeDimension.rows - 1, 0);
        }    
        
        private Grid[][] initGrids(FstPos origin, 
                FstDimension mazeDimension, 
                int gridWidth, int wallThickness) {
            
            Grid[][] mazeGrids = new Grid[mazeDimension.rows][mazeDimension.columns];
            for(int i = 0; i < mazeDimension.rows; i++) {
                for(int j = 0; j < mazeDimension.columns; j++) {
                    int gridUserX = i * gridWidth + wallThickness;
                    int gridUserZ = j * gridWidth + wallThickness;
                    
                    mazeGrids[mazeDimension.rows - i - 1][j] = new Grid(
                        new FstPos(origin.ux + gridUserX, origin.uy, origin.uz + gridUserZ),
                        gridWidth, wallThickness, mazeDimension.layers
                    );
                }
            }
            mazeGrids[0][mazeDimension.columns - 1].wallType = WallType.RIGHT;
            
            return mazeGrids;
        }
        
        private boolean isVisitable(int i, int j) {
            return  i >= 0 && i < grids.length &&
                    j >= 0 && j < grids[0].length &&
                    !grids[i][j].visited;
        }
        
        private int nextI(Dir dir, int i) {
            if(dir == Dir.UP) {
                return i - 1;
            } else if(dir == Dir.DOWN) {
                return i + 1;
            } else {
                return i;
            }
        }
        
        private int nextJ(Dir dir, int j) {
            if(dir == Dir.LEFT) {
                return j - 1;
            } else if(dir == Dir.RIGHT) {
                return j + 1;
            } else {
                return j;
            }
        }    
        
        private void breakRightWallOf(int i, int j) {
            if(grids[i][j].wallType == WallType.UP_RIGHT) {
                grids[i][j].wallType = WallType.UP;
            } else {
                grids[i][j].wallType = WallType.NONE;
            }
        }
        
        private void breakUpWallOf(int i, int j) {
            if(grids[i][j].wallType == WallType.UP_RIGHT) {
                grids[i][j].wallType = WallType.RIGHT;
            } else {
                grids[i][j].wallType = WallType.NONE;
            }        
        }
        
        private void breakLeftWallOf(int i, int j) {
            if(grids[i][j - 1].wallType == WallType.UP_RIGHT) {
                grids[i][j - 1].wallType = WallType.UP;
            } else {
                grids[i][j - 1].wallType = WallType.NONE;
            }        
        }   
        
        private void breakDownWallOf(int i, int j) {
            if(grids[i + 1][j].wallType == WallType.UP_RIGHT) {
                grids[i + 1][j].wallType = WallType.RIGHT;
            } else {
                grids[i + 1][j].wallType = WallType.NONE;
            }        
        }        
        
        private void breakWallBeforeGoing(Dir dir, int i, int j) {
            switch(dir) {
            case UP:
                breakUpWallOf(i, j); break;
            case DOWN:
                breakDownWallOf(i, j); break;
            case LEFT:
                breakLeftWallOf(i, j); break;
            case RIGHT:
                breakRightWallOf(i, j);
            }
        }            
        
        private List<Dir> visitableDirs(int i, int j) {
            List<Dir> dirs = new ArrayList<>();
            for(Dir dir : Dir.values()) {
                if(isVisitable(nextI(dir, i), nextJ(dir, j))) {
                    dirs.add(dir);
                }
            }
            return dirs;
        }
        
        private void visit(int i, int j) {
            grids[i][j].visited = true;
            
            List<Dir> currentVisitableDirs = visitableDirs(i, j);
            if(!currentVisitableDirs.isEmpty()) {
                Collections.shuffle(currentVisitableDirs);
                
                for(Dir dir : currentVisitableDirs) {
                    int nxtI = nextI(dir, i);
                    int nxtJ = nextJ(dir, j);
                    // we need to check again when trying next dir
                    if(isVisitable(nxtI, nxtJ)) {
                        breakWallBeforeGoing(dir, i, j);
                        visit(nxtI, nxtJ);
                    }
                }
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

    public int lengthOfArgs() {
        return 8;
    }
    
    @Override
    public void doCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {      
        int ux = Integer.valueOf(args[0]);
        int uy = Integer.valueOf(args[1]);
        int uz = Integer.valueOf(args[2]);
        
        int rows = Integer.valueOf(args[3]);
        int columns = Integer.valueOf(args[4]);
        
        int gridWidth = Integer.valueOf(args[5]);
        int wallThickness = Integer.valueOf(args[6]);
        int wallHeight = Integer.valueOf(args[7]);  
        
        Commons.runIfAirOrBlockHeld(sender, 
            () -> {  // build maze
                FstDimension mazeDimension = new FstDimension(rows, columns, wallHeight);
                
                buildMaze(sender, 
                    new FstPos(ux, uy, uz),
                    mazeDimension,
                    gridWidth, wallThickness
                );
            }, 
            () -> { // clean maze
                String[] cubeArgs = {
                    args[0], args[1], args[2], 
                    String.valueOf(rows * gridWidth + wallThickness), String.valueOf(columns * gridWidth + wallThickness), args[7]
                };

                Cube cube = new Cube();
                cube.doCommandWithoutCheckingBlock(sender, cubeArgs);
            }
        );
    }
    
    private void buildMaze(ICommandSender sender, 
            FstPos origin, 
            FstDimension mazeDimension,
            int gridWidth, int wallThickness) {

        GridsCreator gridCreator = 
                new GridsCreator(origin, mazeDimension, gridWidth, wallThickness);
        
        Cube cube = new Cube();
        
        buildGrids(sender, cube, gridCreator.grids);
        
        // The most left and bottom walls
        cube.doCommandWithoutCheckingBlock(
            sender, toCubeArgs(origin, 
                       new FstDimension(
                              mazeDimension.rows * gridWidth + wallThickness, 
                              wallThickness, 
                              mazeDimension.layers
                       )
                    )
        );
        cube.doCommandWithoutCheckingBlock(
            sender, toCubeArgs(new FstPos(origin.ux, origin.uy, origin.uz + gridWidth), 
                        new FstDimension(
                              wallThickness, 
                              (mazeDimension.columns - 1) * gridWidth + wallThickness, 
                              mazeDimension.layers
                        )
                    )
        );
    }
    
    private void buildGrids(ICommandSender sender, Cube cube, Grid[][] grids) {
        for(Grid[] rowGrids : grids) {
            for(Grid grid : rowGrids) {
                buildGrid(sender, cube, grid);
            }
        }
    }    
    
    private void buildGrid(ICommandSender sender, Cube cube, Grid grid) {
        int width = grid.width;
        int wallThickness = grid.wallThickness;
        int wallHeight = grid.wallHeight;
        FstPos origin = grid.origin;
        WallType wallType = grid.wallType;
        
        int offset = width - wallThickness;
        
        String[] upWallArgs = toCubeArgs(
            new FstPos(origin.ux + offset, origin.uy, origin.uz), 
            new FstDimension(wallThickness, width, wallHeight)
            
        );
        String[] rightWallArgs = toCubeArgs(
            new FstPos(origin.ux, origin.uy, origin.uz + offset), 
            new FstDimension(width, wallThickness, wallHeight)
        );
        String[] cornerWallArgs =  toCubeArgs(
            new FstPos(origin.ux + offset, origin.uy, origin.uz + offset), 
            new FstDimension(wallThickness, wallThickness, wallHeight)
        );
        
        if(wallType == WallType.UP || wallType == WallType.UP_RIGHT) {
            cube.doCommandWithoutCheckingBlock(sender, upWallArgs);
        }
        if(wallType == WallType.RIGHT || wallType == WallType.UP_RIGHT) {
            cube.doCommandWithoutCheckingBlock(sender, rightWallArgs); 
        }
        if(wallType == WallType.NONE) {
            cube.doCommandWithoutCheckingBlock(sender, cornerWallArgs); 
        }
    }
    
    private static String[] toCubeArgs(FstPos fstPos, FstDimension fstDimension) {
        return new String[ ]{
            String.valueOf(fstPos.ux),
            String.valueOf(fstPos.uy),
            String.valueOf(fstPos.uz),
            String.valueOf(fstDimension.rows),
            String.valueOf(fstDimension.columns),
            String.valueOf(fstDimension.layers)
        };
    }
}
