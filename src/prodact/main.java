package prodact;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Random random = new Random();

		int[] bordnums = { 0, 1, 2, 3 };
		String[] icons = { "〇", "✕" };

		//タイトル		
		//textあとで分けたい

		System.out.println("\n**********************");
		System.out.println("\n\t〇×ゲーム\n");
		System.out.println("**********************\n");

		for (int i = 0; i < bordnums.length; i++) {
			System.out.print(bordnums[i] + "\t");
			if (i == 3) {
				System.out.println("\n");
			}
		}
		for (int i = １; i < bordnums.length; i++) {
			System.out.print(bordnums[i] + "\t");

			System.out.print(icons[RandamIcon(random)] + "\t");
			System.out.print(icons[RandamIcon(random)] + "\t");
			System.out.println(icons[RandamIcon(random)] + "\n");
		}
		Scanner in = new Scanner(System.in);

		//名前入力
		//		System.out.println("あなたの名前を入力してね");
		//		System.out.print("名前：");
		//		String name = in.nextLine();
		//		System.out.println("ようこそ！" + name + "さん");

		//二次元配列で１．１や３．１といった数字で記号を入れる場所を選択できるようにする
		//プレイヤーとプログラムが交互に記号を打ち込む。
		//もともと入ってる場所には記号を入れることはできない
		String[][] array = new String[3][3];

		System.out.println("あなたは〇です。\n自分の番が来たら記号を置く場所を「1,1」のように指定してください。");

		//3つ並ぶか盤面が埋まるまで続ける
		while (true) {

			//cpuの勝利判定
			boolean lost = cpuWin(array);
			if (lost) {
				System.out.println("敗北！");
				break;
			}

			//null数える
			int nulls = findNull(array);
			
			if (nulls == 0) {
				System.out.println("\n引き分け\n");
				break;
			}

			//prayerの番

			System.out.println("あなたの番です");
			//入力した数字を入れる配列
			int[] nums = new int[2];

			while (true) {
				String num = in.nextLine();

				//想定外の入力を防ぐ
				Pattern p = Pattern.compile("[1-3]{1},[1-3]{1}");
				Matcher m = p.matcher(num);
				if (!m.matches()) {
					System.out.println("書き方が違います。「1,1」のように入力してください");
					continue;
				}
				String[] numArray = num.split(",");
				for (int i = 0; i < numArray.length; i++) {
					nums[1] = Integer.parseInt(numArray[i]);
				}

				for (int i = 0; i < numArray.length; i++) {
					nums[i] = Integer.parseInt(numArray[i]) - 1;
					System.out.println(nums[i]);
				}
				if (array[nums[0]][nums[1]] != icons[0] && array[nums[0]][nums[1]] != icons[1]) {
					array[nums[0]][nums[1]] = icons[0];
					break;
				} else {
					System.out.println("\nここには入力できません\n再度入力してください\n");
				}

			}

			//	盤面の表示
			WriteBord(array);

			//playerの勝利判定
			boolean win = playerWin(array);
			if (win) {
				System.out.println("勝利！");
				break;
			}
			//cpuの番
			//playerを邪魔するところに✕を置く
			//隣り合うところがなくなったらNullに入れるようにする
			
			//埋まってないか確認
			nulls = findNull(array);
			System.out.println(nulls);
			if (nulls == 0) {
				System.out.println("\n引き分け\n");
				break;
			}
			System.out.println("\nあいての番です\n");

			int count = 0;

			while (true) {
				count++;
				System.out.println(count + "回目\n");

				//入れるところが見つからなければ空白のところに入れる
				if (count > 9) {
					int[] sell = findSell(array);
					array[sell[0]][sell[1]] = icons[1];
					break;

				} else {
					int length = nums[0];
					int side = nums[1];
					
					//縦の処理
					if (nums[0] == 0) {
						int number = RandamIcon(random);
						length += number;

					} else if (nums[0] == 2) {
						int number = RandamIcon(random);
						length -= number;

					} else {
						int route = RandamIcon(random);
						if (route == 0) {
							int number = RandamIcon(random);
							length += number;
						} else {
							int number = RandamIcon(random);
							length -= number;

						}
					}

					//横の処理
					if (nums[1] == 0) {
						int number = RandamIcon(random);
						side += number;

					} else if (nums[1] == 2) {
						int number = RandamIcon(random);
						side -= number;

					} else {
						int route = RandamIcon(random);
						if (route == 0) {
							int number = RandamIcon(random);
							side += number;
						} else {
							int number = RandamIcon(random);
							side -= number;

						}
					}

					System.out.println(length);
					System.out.println(side + "\n");

					//空白なら代入
					if (array[length][side] == " ") {
						array[length][side] = icons[1];
						break;
					}
				}
			}

			//	盤面の表示
			WriteBord(array);

		}

	}

	//0か1かをランダムに選ぶメソッド
	private static int RandamIcon(Random random) {
		// TODO 自動生成されたメソッド・スタブ

		int nums = random.nextInt(2);
		return nums;
	}

	//二次元配列が埋まっていないかの確認
	public static int findNull(String[][] array) {
		int count = 9;
		for (String[] row : array) {
			for (String value : row) {
				if (value != " " && value != null) {
					count--;
				}
			}

		}
		return count;

	}

	//空白のセルを探す
	public static int[] findSell(String[][] array) {
	String[] icons = { "〇", "✕" };
		int length = 0;
		int side = 0;
		//配列の中で一番後方にある空白のセルに代入となる
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (!(array[i][j].equals(icons[0]))) {
					if (!(array[i][j].equals(icons[1]))) {
						length = i;
						side = j;
					}

				}
			}

		}
		int[] sell = { length, side };

		return sell;
	}

	//playerの勝利
	public static boolean playerWin(String[][] array) {

		String[] icons = { "〇", "✕" };

		if (array[0][0] == icons[0] && array[0][1] == icons[0] && array[0][2] == icons[0]) {
			return true;

		} else if (array[1][0] == icons[0] && array[1][1] == icons[0] && array[1][2] == icons[0]) {
			return true;

		} else if (array[2][0] == icons[0] && array[2][1] == icons[0] && array[2][2] == icons[0]) {
			return true;

		} else if (array[0][0] == icons[0] && array[1][0] == icons[0] && array[2][0] == icons[0]) {
			return true;

		} else if (array[0][1] == icons[0] && array[1][1] == icons[0] && array[2][1] == icons[0]) {
			return true;

		} else if (array[0][2] == icons[0] && array[1][2] == icons[0] && array[2][2] == icons[0]) {
			return true;

		} else if (array[0][0] == icons[0] && array[1][1] == icons[0] && array[2][2] == icons[0]) {
			return true;

		} else if (array[0][2] == icons[0] && array[1][1] == icons[0] && array[2][0] == icons[0]) {
			return true;

		} else {
			return false;

		}

	}

	//cpuの勝利
	public static boolean cpuWin(String[][] array) {

		String[] icons = { "〇", "✕" };

		if (array[0][0] == icons[1] && array[0][1] == icons[1] && array[0][2] == icons[1]) {
			return true;

		} else if (array[1][0] == icons[1] && array[1][1] == icons[1] && array[1][2] == icons[1]) {
			return true;

		} else if (array[2][0] == icons[1] && array[2][1] == icons[1] && array[2][2] == icons[1]) {
			return true;

		} else if (array[0][0] == icons[1] && array[1][0] == icons[1] && array[2][0] == icons[1]) {
			return true;

		} else if (array[0][1] == icons[1] && array[1][1] == icons[1] && array[2][1] == icons[1]) {
			return true;

		} else if (array[0][2] == icons[1] && array[1][2] == icons[1] && array[2][2] == icons[1]) {
			return true;

		} else if (array[0][0] == icons[1] && array[1][1] == icons[1] && array[2][2] == icons[1]) {
			return true;

		} else if (array[0][2] == icons[1] && array[1][1] == icons[1] && array[2][0] == icons[1]) {
			return true;

		} else {
			return false;

		}

	}

	//	盤面の表示
	public static void WriteBord(String[][] array) {
		int[] bordnums = { 0, 1, 2, 3 };
		// TODO 自動生成されたメソッド・スタブ
		for (int i = 0; i < bordnums.length; i++) {
			System.out.print(bordnums[i] + "\t");
			if (i == 3) {
				System.out.println("\n");
			}
		}
		for (int i = 0; i < 3; i++) {
			System.out.print(bordnums[i + 1] + "\t");
			for (int j = 0; j < 3; j++) {
				if (array[i][j] == null) {
					array[i][j] = " ";
				}
				if (j != 2) {
					System.out.print(array[i][j] + "\t");
				} else {
					System.out.println(array[i][j] + "\n");
				}

			}
		}
	}

}
