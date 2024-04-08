package prodact;

import java.util.Scanner;

public class paiza {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		Scanner in =new Scanner(System.in);
		String number = in.nextLine();
		int num = Integer.parseInt(number);
		String[] words = new String[num];
		
		for(int i= 0;i < num ; i++) {
			System.out.println("入力してください");
			String word = in.nextLine();
			words[i] = word;
			System.out.println(words[i]);
		}
		
		int count=1;
		for(int ni= 0; ni <= num;ni++) {
			count++;
			String[] lastAlph = words[ni].split("");
			String[] stAlph = words[ni+1].split("");
				
			if(!(lastAlph[lastAlph.length-1].equals(stAlph[0]))) {
				System.out.println("失敗");
				System.out.println(lastAlph[lastAlph.length-1]);
				System.out.println(stAlph[0]);
				break;
			}
			if(count==num) {
				System.out.println("成功！");
			}
		}
		
	}

}
