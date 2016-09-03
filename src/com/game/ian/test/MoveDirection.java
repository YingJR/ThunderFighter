package com.game.ian.test;

/*  1:   */  enum MoveDirection
/*  2:   */ {
/*  3: 5 */   None(0),  Up(1),  Down(2),  Left(3),  Right(4);
/*  4:   */   
/*  5:11 */   private int _value = -1;
/*  6:   */   
/*  7:   */   private MoveDirection(int value)
/*  8:   */   {
/*  9:14 */     this._value = value;
/* 10:   */   }
/* 11:   */   
/* 12:   */   int get_value()
/* 13:   */   {
/* 14:17 */     return this._value;
/* 15:   */   }
/* 16:   */   
/* 17:   */   MoveDirection getByValue(int value)
/* 18:   */   {
/* 19:20 */     MoveDirection[] directions = values();
/* 20:22 */     for (int i = 0; i < directions.length; i++) {
/* 21:23 */       if (directions[i].get_value() == value) {
/* 22:24 */         return directions[i];
/* 23:   */       }
/* 24:   */     }
/* 25:26 */     return None;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static MoveDirection getRandomDirection()
/* 29:   */   {
/* 30:30 */     int value = (int)(Math.random() * (Right.get_value() - None.get_value() + 1));
/* 31:   */     
/* 32:32 */     return None.getByValue(value);
/* 33:   */   }
/* 34:   */ }


/* Location:           H:\III_FSIT\Java-course\JSP\BigProject\Z\2d_exercise\out\artifacts\2d_exercise_jar\2d_exercise.jar
 * Qualified Name:     MoveDirection
 * JD-Core Version:    0.7.0.1
 */