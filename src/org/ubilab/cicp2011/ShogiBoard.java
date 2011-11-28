package org.ubilab.cicp2011;

import org.apache.commons.collections15.keyvalue.MultiKey;
import org.apache.commons.collections15.map.MultiKeyMap;
import org.apache.commons.collections15.MapIterator;
import org.ubilab.cicp2011.ShogiPos.KanSuji;

/**
 * 将棋盤を表すクラス
 * @author atsushi-o
 * @since 2011/11/23
 */
public class ShogiBoard {
    private static final MultiKeyMap<Integer, ShogiPiece> board;
    private final ShogiPlayer me;
    private final ShogiPlayer opponent;

    static {
        // Over Java7
        //board = new MultiKeyMap<>();
        board = new MultiKeyMap<Integer, ShogiPiece>();
    }

    /**
     * 将棋盤を用意する
     * @param me 自身のプレイヤーID
     * @since 2011/11/25
     */
    public ShogiBoard(ShogiPlayer me) {
        this.me = me;
        this.opponent = ShogiPlayer.SENTE == me?ShogiPlayer.GOTE:ShogiPlayer.SENTE;

        initialize();
    }

    /**
     * 将棋盤を初期化する
     * @since 2011/11/25
     */
    private void initialize() {
        for (int i = 1; i < 10; i++)
            for (int j = 1; j < 10; j++)
                put(i, j, null);

        for (int i = 1; i < 10; i++) {
            put(i, 3, new ShogiPiece("歩", opponent));
            put(i, 7, new ShogiPiece("歩", me));
        }

        String[][] args = {
            {"1", "9", "香"},
            {"2", "8", "桂"},
            {"3", "7", "銀"},
            {"4", "6", "金"}
        };
        for (int i = 0; i < args.length; i++) {
            for (int j = 0; j < 2; j++) {
                put(Integer.valueOf(args[i][j]).intValue(), 1, new ShogiPiece(args[i][2], opponent));
                put(Integer.valueOf(args[i][j]).intValue(), 9, new ShogiPiece(args[i][2], me));
            }
        }
        
        put(8, 2, new ShogiPiece("飛", opponent));
        put(2, 8, new ShogiPiece("飛", me));
        
        put(2, 2, new ShogiPiece("角", opponent));
        put(8, 8, new ShogiPiece("角", me));

        put(5, 1, new ShogiPiece("玉", opponent));
        put(5, 9, new ShogiPiece("玉", me));
    }

    /**
     * MultiKeyMapのputメソッドラッパ
     * @param x 盤面の横方向座標
     * @param y 盤面の縦方向座標
     * @param piece 駒クラス
     * @since 2011/11/25
     */
    private void put(int x, int y, ShogiPiece piece) {
        board.put(Integer.valueOf(x), Integer.valueOf(y), piece);
    }
    
    /**
     * MultiKeyMapのgetメソッドラッパ
     * @param x 盤面の横方向座標
     * @param y 盤面の縦方向座標
     * @return 対応する座標の駒クラス．駒がない場合はnull
     * @since 2011/11/28
     */
    private ShogiPiece get(int x, int y) {
        return board.get(Integer.valueOf(x), Integer.valueOf(y));
    }
    
    /**
     * 現在の盤面状態を出力する
     * @return 現在の盤面状態の文字列表現
     * @since 2011/11/28
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        // 改行コード取得
        String nl = System.getProperty("line.separator");
        
        sb.append("  9   8   7   6  5   4   3  2   1").append(nl);
        sb.append("┌─┬─┬─┬─┬─┬─┬─┬─┬─┐").append(nl);
        for (int i = 1; i < 10; i++) {
            sb.append("│");
            for (int j = 9; j > 0; j--) {
                ShogiPiece p = get(j, i);
                if (p != null)  sb.append(p.getPCharacter());
                else            sb.append("　");
                sb.append("│");
            }
            sb.append(KanSuji.toKanSuji(i)).append(nl);
            if (i < 9) {
                sb.append("├─┼─┼─┼─┼─┼─┼─┼─┼─┤").append(nl);
            }
        }
        sb.append("└─┴─┴─┴─┴─┴─┴─┴─┴─┘").append(nl);
        
        return sb.toString();
    }
    
    public static void main(String args[]) {
        ShogiBoard sb = new ShogiBoard(ShogiPlayer.SENTE);
        System.out.println(sb);
    }
}
