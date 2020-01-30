package lab13_eight_queen;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Eight_queen {
	int[][] board;
	int num_of_queen;
	LinkedList<Integer[]> queen_pos=new LinkedList<Integer[]>();
	static LinkedList<LinkedList<Integer[]>> possibilities=new LinkedList<LinkedList<Integer[]>>();
	static LinkedList<LinkedList<Integer[]>> distinct_pos=new LinkedList<LinkedList<Integer[]>>();
	int board_dimension;
	public Eight_queen(int dimension) {
		board_dimension=dimension;
		board= new int[board_dimension][board_dimension];
		resetBoard();
	}
	public static void main(String[] args) {
		int n=13;
		Eight_queen eq= new Eight_queen(n);
		System.out.println(possibilities.size());
		eq.putNQueensOnBoard(n);
		System.out.println("pos size:"+possibilities.size());
	
		//eq.printBoard();
		eq.ruleOutRotatedSolution();
		System.out.println("distinct pos size:"+distinct_pos.size());
		//eq.printDistinctSolutions();
	}
	public void resetBoard() {
		for(int i=0;i<board_dimension;i++)
			for(int j =0;j<board_dimension;j++)
				board[i][j]=0;
	}
	public void printDistinctSolutions() {
		//set board back to -1
		
		for(LinkedList<Integer[]> l : distinct_pos) {
			resetBoard();
//			System.out.println("l size:"+l.size());
			for(Integer[] i : l) {
				board[i[0]][i[1]]=1;
			}
			printBoard();
			
		}
	}
	public void printBoard() {
		for(int i=0;i<board_dimension;i++) {
			for(int j =0;j<board_dimension;j++) {
				if(i==0 && j==0) {
					System.out.print("  ");
					for(int y=1;y<=board_dimension;y++)
						System.out.print(y+" ");
					System.out.println();
				}
				if(j==0) {
					System.out.print(((char)('a'+i))+" ");
					
				}
				
				if(board[i][j]==0)
					System.out.print("- ");
				else if(board[i][j]<0)
					System.out.print("x ");
				else
					System.out.print("Q ");
					//System.out.print(board[i][j]+" ");
			}
		System.out.println();
		//1,2,3
		//11,	10,12 ,9,8,7,6,5,4
		//https://en.wikipedia.org/wiki/Eight_queens_puzzle#Constructing_and_counting_solutions
		}
	}
	public void putNQueensOnBoard(int n) {
//		board[4][4]=1;//put queen there
//		Integer[] newQueenPos = new Integer[] {4,4};
//		queen_pos.add(newQueenPos);
//		crossOutLastQueensPath();
		//System.out.println("n: "+","+n);
		if(n==0){
//			printBoard();
			System.out.println("fulfilled");
//			printBoard();
			if(queen_pos.size()>0) {
				LinkedList<Integer[]> rec=new LinkedList<Integer[]>();
				for(Integer[] i:queen_pos) {
					rec.add(i);
				}
				possibilities.add(rec);
			}
			
			fillBackInLastQueensPath();
			int x=0,y=0;
			if(queen_pos.size()>0) {
			x=queen_pos.getLast()[0];
			 y=queen_pos.getLast()[1];
			}
			board[x][y]=0;
			System.out.println("qsize:"+queen_pos.size());
			
			if(queen_pos.size()>0) 
				queen_pos.removeLast();
			printBoard();
			
			return;
		}
		int i=Math.min(board_dimension-1,Math.max(board_dimension-n,0));
		System.out.println("i: "+i);
			for(int j =0;j<board_dimension;j++) {
				int x=i,y=j;
				if(board[x][y]==0) {
					board[x][y]=n;//put queen there
					Integer[] newQueenPos = new Integer[] {x,y};
					queen_pos.add(newQueenPos);
					crossOutLastQueensPath();
				//	System.out.println("layer "+n+" ("+i+","+j+")");
					putNQueensOnBoard(n-1);	
				}
				if(y==board_dimension-1){//no more paths,wrong one
					fillBackInLastQueensPath();
				//	System.out.println("return at layer "+n+" ("+i+","+j+")");
					if(queen_pos.size()>0) {
					x=queen_pos.getLast()[0];
					y=queen_pos.getLast()[1];
					}
					board[x][y]=0;
					if(queen_pos.size()>0) 
					queen_pos.removeLast();
					return;
				}
			
		}
	}
	public void fillBackInLastQueensPath() {
		int x=0,y=0;
		if(queen_pos.size()>0) {
		x=queen_pos.getLast()[0];
		y=queen_pos.getLast()[1];
		}
		//-1 original state, 0 crossed out, 1=queen
		//same y same x, x-1 y-1 x+1 y+1  
		for(int i=0;i<board_dimension;i++) {//i=x
			for(int j =0;j<board_dimension;j++) {//j=y
				if(i!=x || j!=y) {
					if(((i==x) || (j==y)						||
							(j-y==i-x)||
							(i-x==y-j))) {
						if(board[i][j]==1) {//queen
							System.out.println("jeopardized queen at ("+i+","+j+")");
						}
						else if(board[i][j]<0)
							board[i][j]++;
					}
					
				}
			
			}
		}
	}
	//store distance between each line vertically and horizontally
//92 -> only 12 
	public void ruleOutRotatedSolution() {
		//check 180 degree
		LinkedList<Integer[]> new_pos = new LinkedList<Integer[]>();
		Set<String> p = new HashSet<String>();
		
		for(LinkedList<Integer[]> l: possibilities) {
//			System.out.println("hey:"+possibilities.size());
//			System.out.println("hey:"+l.size());
			String s="";//vertical
			int[] arr=new int[board_dimension];
			for(int i=0;i<l.size()-1;i++) {
				int dif_v = Math.abs(l.get(i)[1]-l.get(i+1)[1]);
				s+=dif_v;
				//System.out.println("hey:"+l.get(i)[1]+","+l.get(i+1)[1]);
				//get horiziontal
				arr[l.get(i)[1]]=l.get(i)[0];
				if(i+1==l.size()-1) {
					arr[l.get(i+1)[1]]=l.get(i+1)[0];
				}
				
			}
			//get horizontal diff
			String sh="";//horizontal
			for(int i=0;i<arr.length-1;i++) {
				int dif_h = Math.abs(arr[i]-arr[i+1]);
				sh+=dif_h;
				//System.out.println("sh hey:"+arr[i]+","+arr[i+1]);
			}
			String reverse_h="";
			for(int i = sh.length() - 1; i >= 0; i--) {
				reverse_h = reverse_h + sh. charAt(i); }
			
			String reverse="";
			for(int i = s. length() - 1; i >= 0; i--) {
				reverse = reverse + s. charAt(i); }
		//	printPos(l);
//			System.out.println(s+","+reverse+","+sh+","+reverse_h);
			//check -2-3-4-5 2345 5432 upside down and leftside right 
			
			
			if(p.contains(s)||p.contains(reverse)
					||p.contains(sh)||p.contains(reverse_h)) {
				
			}else {
				p.add(s);
				p.add(sh);
//				p.add(reverse);
//				p.add(reverse_h);
				distinct_pos.add(l);
			}
		}
		
		
	}
	public void printPos(LinkedList<Integer[]> l) {
//		if(l.size()>0 && l.size()==board_dimension)
//		System.out.println(l.get(0)[1]+l.get(1)[1]+l.get(2)[1]+l.get(3)[1]+l.get(4)[1]+l.get(5)[1]+l.get(6)[1]+l.get(7)[1]);
	}
	public void crossOutLastQueensPath() {
		int x=0,y=0;
		if(queen_pos.size()>0) {
		x=queen_pos.getLast()[0];
		y=queen_pos.getLast()[1];
		}
		//-1 original state, 0 crossed out, 1=queen
		//same y same x, x-1 y-1 x+1 y+1  
		for(int i=0;i<board_dimension;i++) {//i=x
			for(int j =0;j<board_dimension;j++) {//j=y
				if(i!=x || j!=y) {
					if(((i==x) || (j==y)						||
							(j-y==i-x)||
							(i-x==y-j))) {
						if(board[i][j]==1) {//queen
							System.out.println("jeopardized queen at ("+i+","+j+")");
						}
						else
							board[i][j]--;
					}
					
				}
			
			}
		}
	}
}
