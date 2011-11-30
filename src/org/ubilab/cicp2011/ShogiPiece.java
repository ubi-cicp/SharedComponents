package org.ubilab.cicp2011;

import java.io.Serializable;
import java.util.EnumSet;
import static org.ubilab.cicp2011.ShogiPiece.Direction.*;

/**
 * 将棋の駒を表すクラス
 * @author atsushi-o
 * @since 2011/11/22
 */
public class ShogiPiece implements Serializable {
    /**
     * 方向を表す列挙型
     * 
     * <pre>
     *           ← x
     * |00|01|02|03|04|*ref point
     * |05|06|07|08|09|y
     * |10|11|駒|12|13|↓
     * |14|15|16|17|18|
     * |19|20|21|22|23|
     * </pre>
     * @author atsushi-o
     * @since 2011/11/23
     */
    public enum Direction {
        /**
         * 一つのマスを中心とした5x5のグリッドそれぞれに対応する位置フラグ
         */
        D00 (2,-2), D01 (1,-2), D02 (0,-2), D03 (-1,-2), D04 (-2,-2),
        D05 (2,-1), D06 (1,-1), D07 (0,-1), D08 (-1,-1), D09 (-2, 1),
        D10 (2, 0), D11 (1, 0),             D12 (-1, 0), D13 (-2, 0),
        D14 (2, 1), D15 (1, 1), D16 (0, 1), D17 (-1, 1), D18 (-2, 1),
        D19 (2, 2), D20 (1, 2), D21 (0, 2), D22 (-1, 2), D23 (-2, 2),
        /**
         * グリッドに収まり切らない前方位置
         */
        FORWARD (0, -3),
        /**
         * グリッドに収まり切らない後方位置
         */
        DOWNWARD (0, 3),
        /**
         * グリッドに収まり切らない左方位置
         */
        LEFTWARD (3, 0),
        /**
         * グリッドに収まり切らない右方位置
         */
        RIGHTWARD (-3, 0),
        /**
         * グリッドに収まり切らない斜め位置
         */
        DIAGONAL (3, 3),
        /**
         * その他，割り当て不可能位置
         */
        OTHER (0, 0);
        
        private final int x;
        private final int y;
        
        private Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        /**
         * ある駒を中心とした時の座標の差（右上が基準点）から方向を求める
         * @param x 横方向座標の差
         * @param y 縦方向座標の差
         * @return 対応するDirection
         * @since 2011/11/24
         */
        public static Direction valueOf(int x, int y) {
            int absx = Math.abs(x);
            int absy = Math.abs(y);
            
            // グリッド外かどうかの判定
            if (absx >= 3 || absy >= 3) {
                if (absx >=3 && absy >= 3) {
                    if (absx == absy)   return Direction.DIAGONAL;
                    else                return Direction.OTHER;
                }
                // 横方向のグリッド外位置
                else if (absx >= 3) {
                    if (y == 0) return x>0?Direction.LEFTWARD:Direction.RIGHTWARD;
                    else        return Direction.OTHER;
                }
                // 縦方向のグリッド外位置
                else {
                    if (x == 0) return y<0?Direction.FORWARD:Direction.DOWNWARD;
                    else        return Direction.OTHER;
                }
            }
            
            for (Direction d : values()) {
                if (d.x == x && d.y == y) return d;
            }
            
            throw new IllegalArgumentException();
        }
        
        /**
         * ある駒を中心とした時の座標の差（右上が基準点）から方向を求める
         * @param diff 座標の差を表すShogiPos
         * @return 対応するDirection
         * @since 2011/11/24
         */
        public static Direction valueOf(ShogiPos diff) {
            return Direction.valueOf(diff.x(), diff.y());
        }
        
        /**
         * srcの位置を中心とした時のdstの方向を求める
         * @param src 基準とする位置
         * @param dst 方向を求めたい位置
         * @return 対応するDirection
         * @since 2011/11/24
         */
        public static Direction valueOf(ShogiPos src, ShogiPos dst) {
            ShogiPos diff = src.diff(dst);
            return Direction.valueOf(diff);
        }
    }
    
    /**
     * 将棋駒の種類を表す列挙型
     * @author atsushi-o
     * @since 2011/11/22
     */
    public enum ShogiPieceType {
        /**
         * 王将，玉将
         */
        GYOKU   ("玉", "", EnumSet.of(D06,D07,D08,D11,D12,D15,D16,D17), EnumSet.noneOf(Direction.class), false),
        /**
         * 金将
         */
        KIN     ("金", "", EnumSet.of(D06,D07,D08,D11,D12,D16), EnumSet.noneOf(Direction.class), false),
        /**
         * 銀将
         */
        GIN     ("銀", "成銀", EnumSet.of(D06,D07,D08,D15,D17), EnumSet.of(D11,D12,D15,D16,D17), true),
        /**
         * 桂馬
         */
        KEI     ("桂", "成桂", EnumSet.of(D01,D03), EnumSet.of(D01,D03,D06,D07,D08,D11,D12,D16), true),
        /**
         * 香車
         */
        KYO     ("香", "成香", EnumSet.of(D02,D07), EnumSet.of(D02,D06,D08,D11,D12,D16), true),
        /**
         * 歩兵
         */
        FU      ("歩", "と", EnumSet.of(D07), EnumSet.of(D06,D08,D11,D12,D16), true),
        /**
         * 飛車
         */
        HISHA   ("飛", "竜", EnumSet.of(D02,D07,D10,D11,D12,D13,D16,D21), EnumSet.of(D06,D08,D15,D17), true),
        /**
         * 角行
         */
        KAKU    ("角", "馬", EnumSet.of(D00,D04,D06,D08,D15,D17,D19,D23), EnumSet.of(D07,D11,D12,D16), true);
        
        private final String chara;
        private final String promoteChara;
        private final EnumSet<Direction> move;
        private final EnumSet<Direction> promoteMove;
        private final boolean promotable;
        
        private static final int[][] flagTable;
        
        static {
            flagTable = new int[5][5];
            int count = 0;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (i == 2 && j == 2) {
                        flagTable[i][j] = 0;
                    } else {
                        flagTable[i][j] = 1<<count;
                        count++;
                    }
                }
            }
        }
        
        /**
         * 将棋駒の種類を表す列挙型を生成
         * @param move 通常時の駒の移動可能マス
         * @param promote 成時の駒の移動可能マスのxor
         * @param promotable 成れる駒かどうか
         * @since 2011/11/23
         */
        ShogiPieceType(String name, String promoteName, EnumSet<Direction> move, EnumSet<Direction> promoteMove, boolean promotable) {
            this.chara = name;
            this.promoteChara = promoteName;
            this.move = move;
            this.promoteMove = move ^ promoteMove;
            this.promotable = promotable;
        }
        
        /**
         * 駒の通常時の移動可能マスを表すビット列を返す
         * @return 駒の通常時の移動可能マスを表すEnumSet
         * @since 2011/11/22
         */
        public EnumSet<Direction> move()               { return move; };
        /**
         * 駒の成時の移動可能マスを表すビット列を返す
         * <br>
         * 成れない駒の場合は0を返す
         * @return 駒の成時の移動可能マスを表すビット列
         * @since 2011/11/22
         */
        public EnumSet<Direction> promoteMove()        { return promoteMove; };
        /**
         * 駒が成れるかどうか
         * @return 成れる場合はtrue, そうでない場合はfalse
         * @since 2011/11/22
         */
        public boolean isPromotable()   { return promotable; };
        /**
         * 駒の名前を返す
         * @param p 成っているかどうか
         * @return 駒の名前
         */
        public String getCharacter(boolean p) { return p?promoteChara:chara; }
        
        /**
         * 駒が与えられた場所から場所へ移動できるかどうかを判定する
         * <br>
         * 判定は，縦の位置は数が少ないほうが前方であるとして判定する
         * @param src 駒の現在位置
         * @param dst 駒の移動予定位置
         * @param promote 駒が成っているかどうか
         * @return 駒が移動できるか否か
         * @since 2011/11/22
         */
        public boolean isMovable(ShogiPos src, ShogiPos dst, boolean promote) {
            EnumSet<Direction> flag = promote?this.promoteMove:this.move;
            ShogiPos diff = src.diff(dst);
            
            // 座標の差が3以上の場合はその方向へ移動可能か判定
            if (Math.abs(diff.x()) >= 3 || Math.abs(diff.y()) >= 3) {
                // 両方3以上の場合（角行・竜馬）
                if (Math.abs(diff.x()) >= 3 && Math.abs(diff.y()) >= 3) {
                    return (this == ShogiPieceType.KAKU);
                }
                // どちらか一方が3以上の場合（飛車・竜王・香車）
                else {
                    if (this == ShogiPieceType.KYO)
                        return diff.y()<0?true:false;
                    else if (this == ShogiPieceType.HISHA)
                        return true;
                    else
                        return false;
                }
            }
            
            // それ以外の場合はフラグテーブルを使ってビット列と単純比較
            return ((flag & flagTable[diff.x()+2][diff.y()+2]) > 0)? true:false;
        }
    }
     
    private ShogiPieceType type;
    private boolean isPromote;
    private ShogiPlayer player;
    
    /**
     * 将棋駒をひとつ生成
     * @param type 駒の種類
     * @param player 駒の所有プレーヤ
     * @since 2011/11/23
     */
    public ShogiPiece(ShogiPieceType type, ShogiPlayer player) {
        this.type = type;
        this.isPromote = false;
        this.player = player;
    }
    
    /**
     * 駒の種類を取得
     * @return 駒の種類
     * @since 2011/11/23
     */
    public ShogiPieceType getType() { return this.type; }
    /**
     * 成っているかどうかを取得
     * @return 成っているかどうか
     * @since 2011/11/23
     */
    public boolean isPromote()      { return this.isPromote; }
    /**
     * 駒の所有プレーヤを取得
     * @return 駒の所有プレーヤ
     * @since 2011/11/23
     */
    public ShogiPlayer getPlayer()          { return this.player; }
    /**
     * 駒の所有プレーヤを変更
     * @param p 変更するプレーヤ
     * @since 2011/11/23
     */
    public void setPlayer(ShogiPlayer p)    { this.player = p; }
    /**
     * 駒の名前を取得
     * @return 駒の名前
     * @since 2011/11/23
     */
    public String getCharacter()    { return type.getCharacter(isPromote); }
    /**
     * 成る
     * @since 2011/11/23
     */
    public void promote()           { this.isPromote = true; }
}
