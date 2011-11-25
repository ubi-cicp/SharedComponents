package org.ubilab.cicp2011;

import java.io.Serializable;
import org.ubilab.cicp2011.ShogiPiece.ShogiPieceType;

/**
 * 将棋駒の移動軌跡を表すクラス
 * @author atsushi-o
 * @since 2011/11/23
 */
public class ShogiMove implements Serializable {
    private ShogiPiece piece;
    private ShogiPos src;
    private ShogiPos dst;
    private boolean promote;
    private boolean drop;
    
    /**
     * 移動軌跡を新規作成
     * @param src 移動対象駒
     * @param dst 移動先
     * @param promote 移動先で成るかどうか
     * @since 2011/11/23
     */
    public ShogiMove(ShogiPiece piece, ShogiPos src, ShogiPos dst, boolean promote, boolean drop) {
        this.piece = piece;
        this.src = src;
        this.dst = dst;
        this.promote = promote;
        this.drop = drop;
    }
    
    /**
     * 棋譜形式の文字列から移動軌跡クラスを生成する
     * <br>
     * 表記上，移動元は復元不可能
     * @param str 棋譜形式の文字列
     * @return 対応する移動軌跡クラス
     * @throws IllegalArgumentException 正しくない文字列の場合
     */
    public static ShogiMove valueOf(String str) {
        ShogiPlayer player = ShogiPlayer.parse(""+str.charAt(0));
        ShogiPos pos = ShogiPos.valueOf(str.substring(1, 3));
        ShogiPieceType type = ShogiPieceType.parse(""+str.charAt(3));
        String tmp = str.substring(4);
        boolean promote = false;
        boolean drop = false;
        if (tmp.length() != 0) {
            if (tmp.equals("成")) promote = true;
            else if (tmp.equals("不成")) promote = false;
            else if (tmp.equals("打")) drop = true;
            else throw new IllegalArgumentException();
        }
        
        ShogiPiece piece = new ShogiPiece(type, player);
        return new ShogiMove(piece, null, pos, promote, drop);
    }
    
    /**
     * 棋譜形式の文字列で出力
     * @return 棋譜形式の文字列
     * @since 2011/11/23
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(piece.getPlayer().getCharacter());
        sb.append(dst.toString());
        sb.append(piece.getCharacter());
        sb.append(promote?"成":"不成");
        return sb.toString();
    }
}
