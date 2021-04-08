#include <stdio.h>
#include <Windows.h>
#include <conio.h>
#include <stdlib.h>
#include <string.h>

#define LEN_TOP_SCORE 100
#define START_BOTTOM 30

void print_title();
void playgame(int top_score);
void start_game();
void gotoxy(int x, int y);
void draw_jelly(int height_jelly, int jumping, int lying, int dead);
void print_score(int top_score, int score);
void draw_obstacle1(int loc_obstacle1);
void draw_air_obstacle1(int loc_obstacle1);
void draw_obstacle2(int loc_obstacle2);
void draw_coin(int loc_coin);
void fail(int top_score, int score);

void check_highscore();

char high_score[11][LEN_TOP_SCORE];
int num_high_score[11];
int num_high_score_player = 0;

int main()
{
  system("mode con:cols=100 lines=40");

  while(1){
    system("cls");
    print_title();
    int num_score = 0;
    FILE *top_score_file = fopen("top_score.txt", "r");
    if(top_score_file == NULL){
      printf("There's no player who has top score.\n\n");
      fclose(top_score_file);
      top_score_file = fopen("top_score.txt", "w+");
    }
    else{
      char top_score[LEN_TOP_SCORE];
      memset(top_score, 0, sizeof(top_score));
      fgets(top_score, sizeof(top_score), top_score_file);
      int top_score_len = strlen(top_score);
      if(top_score_len == 0){
        printf("There's no player who has top score.\n\n");
      }
      else{
        printf("Top score player: ");
        char *str_score = strtok(top_score, " ");
        printf("%s", str_score);
        printf("     Score: ");
        str_score = strtok(NULL, " ");
        printf("%s\n", str_score);
        num_score = atoi(str_score);
        printf("\n");
      }
    }
    
    fclose(top_score_file);

    FILE *high_score_file = fopen("high_score.txt", "r");
    num_high_score_player = 0;
    if(high_score_file == NULL){
      fclose(high_score_file);
      high_score_file = fopen("high_score.txt", "w+");
    }
    else{
      while(fgets(high_score[num_high_score_player], sizeof(high_score[num_high_score_player]), high_score_file) != NULL){
        char str_score[LEN_TOP_SCORE];
        strcpy(str_score, high_score[num_high_score_player]);
        char *ptr;
        ptr = strtok(str_score, " ");
        ptr = strtok(NULL, " ");
        num_high_score[num_high_score_player] = atoi(ptr);
        num_high_score_player++;
      }
    }
    fclose(high_score_file);

    printf("Press number...\n\n");
    printf("1. Play Game      ");
    printf("2. Check highscore player       ");
    printf("3. Terminate Game\n\n\n");
    printf("#########################\n");
    printf("#                       #\n");
    printf("#  How to play          #\n");
    printf("#                       #\n");
    printf("#  Space bar: Jump      #\n");
    printf("#  Down key: Lying down #\n");
    printf("#                       #\n");
    printf("#########################\n\n");
    printf("Bottom obstacle:  *\n");
    printf("                 ***\n");
    printf("                *****   Press Space bar to avoid\n\n");
    printf("Air obstacle:   *****\n");
    printf("                 ***\n");
    printf("                  *     Press Down key to avoid\n\n");
    printf("Coin:            ");
    SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 14);
    printf(" @\n");
    printf("                 @@@\n");
    printf("                  @");
    SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 7);
    printf("      Get 100 points\n");


    while(1){
      char press_num = _getch();
      if(press_num == '1'){
        playgame(num_score);
        break;
      }
      else if(press_num == '2'){
        check_highscore();
        break;
      }
      else if(press_num == '3'){
        system("cls");
        print_title();
        printf("Terminate game after 3 seconds\n");
        printf("3...\n");
        Sleep(1000);
        printf("2...\n");
        Sleep(1000);
        printf("1...\n");
        Sleep(1000);
        return 0;
      }      
    }
  }
  return 0;
}

void print_title(){
  printf("   ############        #####    ######     #        #      #     #  ######   #    #    #      #\n");
  printf("  #            #         #      #          #        #       #   #   #    #   #    #    # #    #\n");
  printf(" #   >      <   #        #      ######     #        #         #     ######   #    #    #   #  #\n");
  printf(" #       .      #     #  #      #          #        #         #     #   #    #    #    #     ##\n");
  printf(" #              #       ##      ######     ######   ######    #     #    #   ######    #      #\n");
  printf("  ##############\n");
  printf("\n");
  printf("\n");
  return;
}

void playgame(int top_score){

  int loc_jelly = 3;

  int bottom = START_BOTTOM;
  char press_key;
  int jumping = 0;
  int air = 5;
  int jump_flag = 0;
  int lying = 0;

  int score = 0;

  int obstacle1_flag = 0;
  int loc_obstacle1;
  int obs1_rand_num;
  int loc_obstacle2;
  int obstacle2_flag = 0;

  int coin_flag = 0;
  int loc_coin;
  int coin_rand_num;

  int sleep_time = 100;
  int sleep_flag = 0;

  int dead = 0;

  start_game();
  while(1){
    press_key = '\0';
    system("cls");
    print_title();
    gotoxy(0, 9);
    if(sleep_time == 10) printf("Speed: Lv. MAX");
    else printf("Speed: Lv. %d\n", (100-sleep_time) / 10 + 1);

    print_score(top_score, score);
    score++;
    sleep_flag = score % 300;

    if(obstacle1_flag == 0){
      loc_obstacle1 = 35;
      obs1_rand_num = rand() % 2;
      if(obs1_rand_num == 0){
        obstacle1_flag = 1;
      }
      else{
        obstacle1_flag = 2;
      }
    }
    else{
      if(obstacle1_flag == 1) draw_obstacle1(loc_obstacle1);
      else draw_air_obstacle1(loc_obstacle1);
    }

    if(loc_obstacle1 == 15){
      if(coin_flag == 0){
        coin_rand_num = rand() % 2;
        if(coin_rand_num == 1){
          coin_flag = 1;
          loc_coin = 35;
        }
        else{
          loc_obstacle2 = 35;
          obstacle2_flag = 1;
        }
      }
    }
    if(obstacle2_flag == 1){
      draw_obstacle2(loc_obstacle2);
    }

    if(coin_flag == 1){
      draw_coin(loc_coin);
    }

    draw_jelly(bottom, jumping, lying, dead);
    if(dead) break;
    Sleep(sleep_time);

    if(_kbhit()){
      press_key = _getch();
    }
    if(press_key == ' ' && !jumping){
      jumping = 1;
      jump_flag = 1;
    }
    if(press_key == -32){
      press_key = _getch();
      if(press_key == 80){

        lying = 10;
      }
    }
    else{
      if(lying > 0){
        lying--;
      }
    }

    if(jumping && jump_flag){
      air--;
      bottom = START_BOTTOM+air-5;
      if(air == 0) jump_flag = 0;
    }
    else if(jumping && !jump_flag){
      air++;
      bottom = START_BOTTOM+air-5;
      if(air == 5){
        jump_flag = 1;
        jumping = 0;
        bottom = START_BOTTOM;
      }
    }
    if(!jumping){
      if(loc_obstacle1 <= loc_jelly + 2 && loc_obstacle1 >= loc_jelly - 2 && obstacle1_flag == 1){
        dead = 1;
      }
      if(loc_obstacle2 <= loc_jelly + 2 && loc_obstacle2 >= loc_jelly - 2){
        dead = 1;
      }
    }
    if(!lying){
      if(loc_obstacle1 <= loc_jelly + 1 && loc_obstacle1 >= loc_jelly - 1 && obstacle1_flag == 2){
        dead = 1;
      }
    }

    if(jumping && coin_flag){
      if(loc_coin <= loc_jelly + 2 && loc_coin >= loc_jelly - 2){
        score += 100;
        sleep_flag += 100;
        coin_flag = 0;
      }
    }
    if(obstacle1_flag != 0){
      if(loc_obstacle1 == 0) obstacle1_flag = 0;
      else loc_obstacle1--;
    }
    if(obstacle2_flag == 1){
      if(loc_obstacle2 == 0) obstacle2_flag = 0;
      else loc_obstacle2--;
    }
    if(coin_flag == 1){
      if(loc_coin == 0) coin_flag = 0;
      else loc_coin--;
    }
    if(sleep_flag >= 299 && sleep_time > 10){
      sleep_time -= 10;
      sleep_flag = 0;
    }
  }
  fail(top_score, score);
  return;
}

void start_game(){
  system("cls");
  print_title();
  printf("Ready for game...\n");
  printf("3...\n");
  Sleep(1000);
  printf("2...\n");
  Sleep(1000);
  printf("1...\n");
  Sleep(1000);
  printf("GO!!\n");
  Sleep(300);
  return;
}

void gotoxy(int x, int y){
  COORD c = {2*x, y};
  SetConsoleCursorPosition(GetStdHandle(STD_OUTPUT_HANDLE),c);
  return;
}

void draw_jelly(int height_jelly, int jumping, int lying, int dead){
  SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 13);
  static int jelly_motion = 0;
  if(dead){
    SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 7);
    gotoxy(0, height_jelly-5);
    printf("   #######\n");
    printf("  # x   x #\n");
    printf("  #  ---  #\n");
    printf("   #######\n");
    return;
  }
  if(jumping){
    gotoxy(0, height_jelly-5);
    printf("   #######\n");
    printf("  # >   < #\n");
    printf("  #   .   #\n");
    printf("   #######\n");
  }
  else if(lying){
    gotoxy(0, height_jelly-4);
    printf("   #######\n");
    printf("  # > . < #\n");
    printf("   #######\n");
  }
  else{
    if(!jelly_motion){
      gotoxy(0, height_jelly-5);
      printf("   #######\n");
      printf("  # >   < #\n");
      printf("  #   .   #\n");
      printf("   #######\n");
      jelly_motion = 1;
    }
    else if(jelly_motion){
      gotoxy(0, height_jelly-4);
      printf("   #######\n");
      printf("  # > . < #\n");
      printf("   #######\n");
      jelly_motion = 0;
    }
  }
  SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 7);
  return;
}

void print_score(int top_score, int score){
  gotoxy(35, 7);
  printf("top score: %d\n", top_score);
  gotoxy(0, 7);
  printf("Score: %d", score);
  return;
}

void draw_obstacle1(int loc_obstacle1){
  gotoxy(loc_obstacle1, 26);
  printf("  *\n");
  gotoxy(loc_obstacle1, 27);
  printf(" ***\n");
  gotoxy(loc_obstacle1, 28);
  printf("*****\n");
  return;
}

void draw_air_obstacle1(int loc_obstacle1){
  gotoxy(loc_obstacle1, 23);
  printf("*****\n");
  gotoxy(loc_obstacle1, 24);
  printf(" ***\n");
  gotoxy(loc_obstacle1, 25);
  printf("  *\n");
  return;
}

void draw_obstacle2(int loc_obstacle2){
  gotoxy(loc_obstacle2, 26);
  printf("  *\n");
  gotoxy(loc_obstacle2, 27);
  printf(" ***\n");
  gotoxy(loc_obstacle2, 28);
  printf("*****\n");
  return;
}

void draw_coin(int loc_coin){
  SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 14);
  static int coin_motion = 0;
  if(!coin_motion){
    gotoxy(loc_coin, 21);
    printf(" @\n");
    gotoxy(loc_coin, 22);
    printf("@@@\n");
    gotoxy(loc_coin, 23);
    printf(" @\n");
    coin_motion = 1;
  }
  else{
    gotoxy(loc_coin, 21);
    printf(" @\n");
    gotoxy(loc_coin, 22);
    printf(" @\n");
    gotoxy(loc_coin, 23);
    printf(" @\n");
    coin_motion = 0;
  }
  SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 7);
  return;
}

void fail(int top_score, int score){
  Sleep(1000);
  system("cls");
  print_title();
  printf("score: %d\n\n", score);

  char id[100];
  if(score > top_score){
    printf("Congratulation!! You got a top score!!\n\n");
    printf("Enter your name (Space is not allowed)\n");
    while(1){
      fgets(id, 100, stdin);
      if(strchr(id, ' ') == NULL) break;
      printf("Please remove the space.\n");
    }
    id[strlen(id)-1] = '\0';
    strcat(id, " ");
    FILE *top_score_file = fopen("top_score.txt", "w");
    char new_top_score[15];
    sprintf(new_top_score, "%d", score);
    strcat(id, new_top_score);
    fputs(id, top_score_file);
    fclose(top_score_file);

    FILE *high_score_file = fopen("high_score.txt", "w");
    strcat(id, "\n");
    fputs(id, high_score_file);
    for(int i = 0; i < num_high_score_player; i++){
      if(i == 9) break;
      fputs(high_score[i], high_score_file);
    }
    fclose(high_score_file);
  }
  else{
    int i, rank = -1;
    for(i = 0; i < num_high_score_player; i++){
      if(score > num_high_score[i]){
        rank = i;
        break;
      }
    }
    if(rank == -1){
      if(num_high_score_player != 10){
        FILE *high_score_file = fopen("high_score.txt", "w");
        printf("You got %d ranks!!\n\n", num_high_score_player+1);
        printf("Enter your name (Space is not allowed)\n");
        while(1){
          fgets(id, 100, stdin);
          if(strchr(id, ' ') == NULL) break;
          printf("Please remove the space.\n");
        }
        id[strlen(id)-1] = '\0';
        strcat(id, " ");
        char new_high_score[15];
        sprintf(new_high_score, "%d", score);
        strcat(id, new_high_score);
        strcat(id, "\n");
        for(int i = 0; i < num_high_score_player; i++){
          fputs(high_score[i], high_score_file);
        }
        fputs(id, high_score_file);
        fclose(high_score_file);
      }
      else{
        printf("FAIL\n");
        FILE *high_score_file = fopen("high_score.txt", "w");
        for(int i = 0; i < num_high_score_player; i++){
          fputs(high_score[i], high_score_file);
        }
        fclose(high_score_file);
      }
    }
    else{
      FILE *high_score_file = fopen("high_score.txt", "w");
      printf("You got %d ranks!!\n\n", rank+1);
      printf("Enter your name (Space is not allowed)\n");
      while(1){
        fgets(id, 100, stdin);
        if(strchr(id, ' ') == NULL) break;
        printf("Please remove the space.\n");
      }
      id[strlen(id)-1] = '\0';
      strcat(id, " ");
      char new_high_score[15];
      sprintf(new_high_score, "%d", score);
      strcat(id, new_high_score);
      strcat(id, "\n");
      for(int i = 0; i < rank; i++){
        fputs(high_score[i], high_score_file);
      }
      fputs(id, high_score_file);
      for(int i = rank; i < num_high_score_player; i++){
        if(i == 9) break;
        fputs(high_score[i], high_score_file);
      }
      fclose(high_score_file);
    }
  }
  printf("\n If you want to go to main page, press 'r'\n");
  while(1){
    if(_kbhit()){
      if(_getch() == 'r') break;
    }
  }
  return;
}

void check_highscore(){
  system("cls");
  print_title();
  printf("Highscore Player\n\n");
  
  if(num_high_score_player == 0){
    printf("There're no players who have high score.\n\n");
  }
  else{
    for(int i = 0; i < num_high_score_player; i++){
      printf("%d. %s\n", i+1, high_score[i]);
    }
  }
      
  printf("\n If you want to go to main page, press 'r'\n");
  while(1){
    if(_kbhit()){
      if(_getch() == 'r') break;
    }
  }
  return;
}