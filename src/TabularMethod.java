import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.util.Arrays;

public class TabularMethod {

	public static void sortbyColumn(int arr[][], int col) {
        Arrays.sort(arr, new Comparator<int[]>() {
          public int compare(final int[] entry1,final int[] entry2) {
            if (entry1[col] > entry2[col])
                return 1;
            else
                return -1;
          }
        });
    }

	public static void main (String[] args) {
		char YorN= 'Y';
		while(YorN=='Y' || YorN=='y') {
			System.out.print("Enter the number of variables: ");
			Scanner sc = new Scanner(System.in);
			int variables = sc.nextInt();
			while(variables < 2 || variables > 12) {
				System.out.println("Number of varaibles must be between 2 and 12 ");
				System.out.print("Enter the number of variables: ");
				variables = sc.nextInt();
			}
			int size=0, countingTerms=0;
			int minterms[] = new int[(int)Math.pow(2, variables)+1];
			System.out.println();
			System.out.println("Enter your minterms and enter \"}\" to end the input");
			System.out.print("{ ");
			for(int i=0; i<= ((int)Math.pow(2, variables)); i++) {
				if(sc.hasNextInt()){
					minterms[i]=sc.nextInt();
				}
				else if (sc.next().equals("}")) {
					minterms[i]=-1;
					break;
				}
				while(minterms[i]>=(int)Math.pow(2, variables) || minterms[i]<0) {
					System.out.println("ERROR The minterms are between 0 and "+ (((int)Math.pow(2, variables))-1));
					if(sc.hasNextInt()){
						minterms[i]=sc.nextInt();
					}
					else if (sc.next().equals("}")) {
						minterms[i]=-1;
						break;
					}
				}
				size++;
				countingTerms++;
			}
			int dontcare[] = new int[(int)Math.pow(2, variables)-size+1];
			System.out.println("Enter your don't care and enter \"}\" to end the input");
			System.out.print("{ ");
			for(int i=0; i<= (((int)Math.pow(2, variables))-size); i++) {
				if(sc.hasNextInt()){
					dontcare[i]=sc.nextInt();
				}
				else if (sc.next().equals("}")) {
					dontcare[i]=-1;
					break;
				}
				while(dontcare[i]>=(int)Math.pow(2, variables) || dontcare[i]<0) {
					System.out.println("ERROR The don't care are between 0 and "+ (((int)Math.pow(2, variables))-1));
					if(sc.hasNextInt()){
						dontcare[i]=sc.nextInt();
					}
					else if (sc.next().equals("}")) {
						dontcare[i]=-1;
						break;
					}
				}
				countingTerms++;
			}

			int MAX=((int)Math.pow(3, variables))*((int)Math.pow(2, variables -2))+1;
			int terms[]= new int[countingTerms];
			int i = 0;
			while(minterms[i]!= -1) {
				terms[i]=minterms[i];
				i++;
			}
			int j=0;
			while(i<countingTerms) {
				terms[i]=dontcare[j];
				j++;
				i++;
			}
			Arrays.sort(terms);
			int processed[][]=new int [MAX][3];
			for(i=0; i<countingTerms;i++) {
				processed[i][0]=terms[i];
			}
			int XOR = 0;
			int k = countingTerms;
			for(i=0;i<MAX; i++) {
				for(j=i+1 ;j<MAX; j++) {
					if(processed[i][1] == processed[j][1]) {
						if(processed[i][0]==processed[j][0]) {
							break;
						}else {
							XOR = (processed[i][0] ^ processed[j][0]);
							if((XOR & (XOR - 1 )) == 0 && XOR == processed[i][0]){
								break;
							}
							else if((XOR & (XOR - 1 )) == 0) {
								processed[k][0] = processed[i][0];
								processed[k][1] = processed[i][1]+ XOR;
								processed[i][2] = 1;
								processed[j][2] = 1;
								k++;
							}
						}
					}
				}
			}
			int newProcessed[][]=new int[k][3];
			for(i=0;i<k;i++) {
				newProcessed[i][0]=processed[i][0];
				newProcessed[i][1]=processed[i][1];
				newProcessed[i][2]=processed[i][2];
			}
			for(i=0;i<k;i++) {
				for(j=i+1;j<k;j++) {
					if(newProcessed[i][0]==newProcessed[j][0]) {
						if(newProcessed[i][1]==newProcessed[j][1]) {
							if(newProcessed[i][2]==1 || newProcessed[j][2]==1) {
								newProcessed[i][2]=1;
								newProcessed[j][2]=1;
							}
						}
					}
				}
			}
			j=0;
			int oldPI[][]=new int [k][2];
			for(i=0; i<k; i++) {
				if(newProcessed[i][2]==0) {
					oldPI[j][0]=newProcessed[i][0];
					oldPI[j][1]=newProcessed[i][1];
					j++;
				}
			}
			int sortedPI[][]=new int[j][2];
			for(i=0;i<j;i++) {
				sortedPI[i][0]=oldPI[i][0];
				sortedPI[i][1]=oldPI[i][1];
			}
			sortbyColumn(sortedPI,0);
			for(i=0;i<j;i++) {
				for(k=i+1;k<j;k++) {
					if(sortedPI[i][0]==sortedPI[k][0]) {
						if(sortedPI[i][1]==sortedPI[k][1]) {
							for(int m= k; m < j-1 ;m++) {
								sortedPI[m][0]=sortedPI[m+1][0];
								sortedPI[m][1]=sortedPI[m+1][1];
							}
							j--;
							k--;
						}
					}
				}
			}
			int PI[][]=new int[j][2];
			for(i=0;i<j;i++) {
				PI[i][0]=sortedPI[i][0];
				PI[i][1]=sortedPI[i][1];
			}
			int table[][]= new int [j+1][size+1];
			table[0][0]=-'n';
			for(i=0; i <size;i++) {
				table[0][i+1]=minterms[i];
			}
			for(i=0; i <j;i++) {
				table[i+1][0]=PI[i][0];
			}
			for(i=0; i<j;i++) {
				for(k=1; k<=size; k++) {
					for(int l=0; l<=PI[i][1]; l++) {
						if(table[0][k]==((l&PI[i][1])+table[i+1][0])) {
							table[i+1][k]=-'x';
						}
					}
				}
			}
			int u, v, w;
			int ess=0;
			int essPI[][]=new int [j][2];
			int count=0;
			int repeat=0;
			for(repeat=0; repeat<size; repeat++) {
				for(u=1; u<=j; u++) {
					for(v=1; v<=size; v++) {
						count=0;
						if(table[u][v]==-'x') {
							for(w=0; w<=j; w++) {
								if(table[w][v]==-'x') {
									count++;
								}
							}
							if(count==1) {
								ess++;
								essPI[u-1][0]=PI[u-1][0];
								essPI[u-1][1]=PI[u-1][1];
								for(int m=0; m<=size; m++) {
									if(table[u][m]==-'x' && m!=v) {
										for(int n=0; n<=j; n++) {
											table[n][m]=-1;
										}
									}
									table[u][m]=-1;
								}
								for(int n=0; n<=j; n++) {
									table[n][v]=-1;
								}
							}
						}
					}
				}
			}
			int column[]=new int[j];
			int tempColumn[]=new int[j];
			int tempRow[] = new int [size];
			int row[] = new int [size];
			int x1=0,y1=0,x2=0,y2=0, r,c;
			int colCount1=0, colCount2=0, rowCount1=0, rowCount2=0;
			repeat=0;
			while(repeat<2) {
				for(i=1; i<=j; i++) {
					r=1;
					for(k=1; k<=size;k++) {
						c=1;
						colCount1=0;
						colCount2=0;
						rowCount1=0;
						rowCount2=0;
						if(table[i][k]==-'x') {
							while(r<j+1) {
								for(y1=0; y1<size; y1++) {
									row[y1]=table[i][y1+1];
									if(row[y1]==-'x') {
										rowCount1++;
									}
								}
								if(r==i) {
									r++;
								}
								if(r==j || r > j) {
									break;
								}
								for(y1=0; y1<size; y1++) {
									tempRow[y1]=table[r][y1+1];
									if(tempRow[y1]==-'x') {
										rowCount2++;
									}
								}
								if(Arrays.equals(tempRow, row)) {
									for(y2=0; y2<=size; y2++) {
										table[r][y2]=-1;
									}
									for(u=1; u<=j; u++) {
										for(v=1; v<=size; v++) {
											count=0;
											if(table[u][v]==-'x') {
												for(w=0; w<=j; w++) {
													if(table[w][v]==-'x') {
														count++;
													}
												}
												if(count==1) {
													ess++;
													essPI[u-1][0]=PI[u-1][0];
													essPI[u-1][1]=PI[u-1][1];
													for(int m=0; m<=size; m++) {
														if(table[u][m]==-'x' && m!=v) {
															for(int n=0; n<=j; n++) {
																table[n][m]=-1;
															}
														}
														table[u][m]=-1;
													}
													for(int n=0; n<=j; n++) {
														table[n][v]=-1;
													}
												}
											}
										}
									}
								}
								else if(rowCount2>rowCount1) {
									for(y2=0; y2<size; y2++) {
										if(row[y2]==-'x' && tempRow[y2]==-'x') {
											row[y2]=-1;
											tempRow[y2]=-1;
										}
										else if(row[y2]==0 && tempRow[y2]==-'x') {
											row[y2]=-1;
											tempRow[y2]=-1;
										}
									}
									if(Arrays.equals(tempRow, row)) {
										for(y2=0; y2<=size; y2++) {
											table[i][y2]=-1;
										}
									}
									for(u=1; u<=j; u++) {
										for(v=1; v<=size; v++) {
											count=0;
											if(table[u][v]==-'x') {
												for(w=0; w<=j; w++) {
													if(table[w][v]==-'x') {
														count++;
													}
												}
												if(count==1) {
													ess++;
													essPI[u-1][0]=PI[u-1][0];
													essPI[u-1][1]=PI[u-1][1];
													for(int m=0; m<=size; m++) {
														if(table[u][m]==-'x' && m!=v) {
															for(int n=0; n<=j; n++) {
																table[n][m]=-1;
															}
														}
														table[u][m]=-1;
													}
													for(int n=0; n<=j; n++) {
														table[n][v]=-1;
													}
												}
											}
										}
									}
								}
								else if(rowCount2<rowCount1) {
									for(y2=0; y2<size; y2++) {
										if(row[y2]==-'x' && tempRow[y2]==-'x') {
											row[y2]=-1;
											tempRow[y2]=-1;
										}
										else if(row[y2]==-'x' && tempRow[y2]==0) {
											row[y2]=-1;
											tempRow[y2]=-1;
										}
									}
									if(Arrays.equals(tempRow, row)) {
										for(y2=0; y2<=size; y2++) {
											table[r][y2]=-1;
										}
										for(u=1; u<=j; u++) {
											for(v=1; v<=size; v++) {
												count=0;
												if(table[u][v]==-'x') {
													for(w=0; w<=j; w++) {
														if(table[w][v]==-'x') {
															count++;
														}
													}
													if(count==1) {
														ess++;
														essPI[u-1][0]=PI[u-1][0];
														essPI[u-1][1]=PI[u-1][1];
														for(int m=0; m<=size; m++) {
															if(table[u][m]==-'x' && m!=v) {
																for(int n=0; n<=j; n++) {
																	table[n][m]=-1;
																}
															}
															table[u][m]=-1;
														}
														for(int n=0; n<=j; n++) {
															table[n][v]=-1;
														}
													}
												}
											}
										}
									}
								}
								r++;
								rowCount1=0;
								rowCount2=0;
							}
							while(c<size+1) {
								for(x1=0; x1<j ;x1++) {
									column[x1]=table [x1+1][k];
									if(column[x1]==-'x') {
										colCount1++;
									}
								}
								if(c==k) {
									c++;
								}
								if(c==size || c > size) {
									break;
								}
								for(x1=0; x1<j; x1++) {
									tempColumn[x1]=table[x1+1][c];
									if(tempColumn[x1]==-'x') {
										colCount2++;
									}
								}
								if(Arrays.equals(tempColumn, column)) {
									for(x2=0; x2<=j; x2++) {
										table[x2][c]=-1;
									}
									for(u=1; u<=j; u++) {
										for(v=1; v<=size; v++) {
											count=0;
											if(table[u][v]==-'x') {
												for(w=0; w<=j; w++) {
													if(table[w][v]==-'x') {
														count++;
													}
												}
												if(count==1) {
													ess++;
													essPI[u-1][0]=PI[u-1][0];
													essPI[u-1][1]=PI[u-1][1];
													for(int m=0; m<=size; m++) {
														if(table[u][m]==-'x' && m!=v) {
															for(int n=0; n<=j; n++) {
																table[n][m]=-1;
															}
														}
														table[u][m]=-1;
													}
													for(int n=0; n<=j; n++) {
														table[n][v]=-1;
													}
												}
											}
										}
									}
								}
								else if(colCount2>colCount1) {
									for(x2=0; x2<j; x2++) {
										if(column[x2]==-'x' && tempColumn[x2]==-'x') {
											column[x2]=-1;
											tempColumn[x2]=-1;
										}
										else if(column[x2]==0 && tempColumn[x2]==-'x') {
											column[x2]=-1;
											tempColumn[x2]=-1;
										}
									}
									if(Arrays.equals(tempColumn, column)) {
										for(x2=0; x2<=j; x2++) {
											table[x2][c]=-1;
										}
										for(u=1; u<=j; u++) {
											for(v=1; v<=size; v++) {
												count=0;
												if(table[u][v]==-'x') {
													for(w=0; w<=j; w++) {
														if(table[w][v]==-'x') {
															count++;
														}
													}
													if(count==1) {
														ess++;
														essPI[u-1][0]=PI[u-1][0];
														essPI[u-1][1]=PI[u-1][1];
														for(int m=0; m<=size; m++) {
															if(table[u][m]==-'x' && m!=v) {
																for(int n=0; n<=j; n++) {
																	table[n][m]=-1;
																}
															}
															table[u][m]=-1;
														}
														for(int n=0; n<=j; n++) {
															table[n][v]=-1;
														}
													}
												}
											}
										}
									}
								}
								else if(colCount2<colCount1) {
									for(x2=0; x2<j; x2++) {
										if(column[x2]==-'x' && tempColumn[x2]==-'x') {
											column[x2]=-1;
											tempColumn[x2]=-1;
										}
										else if(column[x2]==-'x' && tempColumn[x2]==0) {
											column[x2]=-1;
											tempColumn[x2]=-1;
										}
									}
									if(Arrays.equals(tempColumn, column)) {
										for(x2=0; x2<=j; x2++) {
											table[x2][k]=-1;
										}
										for(u=1; u<=j; u++) {
											for(v=1; v<=size; v++) {
												count=0;
												if(table[u][v]==-'x') {
													for(w=0; w<=j; w++) {
														if(table[w][v]==-'x') {
															count++;
														}
													}
													if(count==1) {
														ess++;
														essPI[u-1][0]=PI[u-1][0];
														essPI[u-1][1]=PI[u-1][1];
														for(int m=0; m<=size; m++) {
															if(table[u][m]==-'x' && m!=v) {
																for(int n=0; n<=j; n++) {
																	table[n][m]=-1;
																}
															}
															table[u][m]=-1;
														}
														for(int n=0; n<=j; n++) {
															table[n][v]=-1;
														}
													}
												}
											}
										}
									}
								}
								c++;
								colCount1=0;
								colCount2=0;
							}
						}
					}
				}
				repeat++;
				for(u=1; u<=j; u++) {
					for(v=1; v<=size; v++) {
						count=0;
						if(table[u][v]==-'x') {
							for(w=0; w<=j; w++) {
								if(table[w][v]==-'x') {
									count++;
								}
							}
							if(count==1) {
								ess++;
								essPI[u-1][0]=PI[u-1][0];
								essPI[u-1][1]=PI[u-1][1];
								for(int m=0; m<=size; m++) {
									table[u][m]=-1;
								}
								for(int n=0; n<=j; n++) {
									table[n][v]=-1;
								}
							}
						}
					}
				}
			}
			int finalEssPI[][]=new int[ess][2];
			int e=0;
			for(i=0; i<j; i++) {
				if(essPI[i][0]==0 && essPI[i][1]==0) {
					continue;
				}
				else {
					finalEssPI[e][0]=essPI[i][0];
					finalEssPI[e][1]=essPI[i][1];
					e++;
				}
			}
			char[] symbol = new char[variables];
			for(i=0; i<variables; i++) {
				symbol[i]=(char)('A'+i);
			}
			int bin;
			int binaryPI[][]= new int[2][variables];
			System.out.println();
			System.out.print("PI = ");
			for(k=0; k<j; k++) {
				bin=PI[k][0];
				for(int n=variables-1; n>=0; n--) {
					binaryPI[0][n]=bin%2;
					bin=bin/2;
				}
				bin=PI[k][1];
				for(int n=variables-1; n>=0; n--) {
					binaryPI[1][n]=bin%2;
					bin=bin/2;
				}
				for(i=0; i<variables;i++) {
					if(binaryPI[1][i]==0) {
						if(binaryPI[0][i]==1) {
							System.out.print(symbol[i]);
						}else if(binaryPI[0][i]==0){
							System.out.print(symbol[i]+"'");
						}
					}
				}
				if(k<j-1) {
					System.out.print(", ");
				}
			}
			System.out.println();
			System.out.print("Essential PI = ");
			for(k=0; k<ess; k++) {
				bin=finalEssPI[k][0];
				for(int n=variables-1; n>=0; n--) {
					binaryPI[0][n]=bin%2;
					bin=bin/2;
				}
				bin=finalEssPI[k][1];
				for(int n=variables-1; n>=0; n--) {
					binaryPI[1][n]=bin%2;
					bin=bin/2;
				}
				for(i=0; i<variables;i++) {
					if(binaryPI[1][i]==0) {
						if(binaryPI[0][i]==1) {
							System.out.print(symbol[i]);
						}else if(binaryPI[0][i]==0){
							System.out.print(symbol[i]+"'");
						}
					}
				}
				if(k<ess-1) {
					System.out.print(", ");
				}
			}
			System.out.println();
			System.out.println();
			System.out.print("F(");
			for(i=0;i<variables; i++) {
				System.out.print(symbol[i]);
				if(i<variables-1) {
					System.out.print(",");
				}
			}
			System.out.print(")= ");
			for(k=0; k<ess; k++) {
				bin=finalEssPI[k][0];
				for(int n=variables-1; n>=0; n--) {
					binaryPI[0][n]=bin%2;
					bin=bin/2;
				}
				bin=finalEssPI[k][1];
				for(int n=variables-1; n>=0; n--) {
					binaryPI[1][n]=bin%2;
					bin=bin/2;
				}
				for(i=0; i<variables;i++) {
					if(binaryPI[1][i]==0) {
						if(binaryPI[0][i]==1) {
							System.out.print(symbol[i]);
						}else if(binaryPI[0][i]==0){
							System.out.print(symbol[i]+"'");
						}
					}
				}
				if(k<ess-1) {
					System.out.print(" + ");
				}
			}
			System.out.println();
			System.out.println();
			System.out.print("Do you want to continue? [Y/N]");
			YorN=sc.next().charAt(0);
			System.out.println();
		}
	}
}
