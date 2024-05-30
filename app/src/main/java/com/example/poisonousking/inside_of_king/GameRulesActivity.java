package com.example.poisonousking.inside_of_king;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.poisonousking.R;
import com.example.poisonousking.helper_classes.HideTheBars;

public class GameRulesActivity extends AppCompatActivity {

    MediaPlayer buttonClickSound;
    private static final float BACKGROUND_MUSIC_VOLUME = 0.35f; // Set volume level to 35% for background music
    TextView intro, intro_in_rules, quick_game_in_rules, case_1, case_2, points_in_rules, coins_in_rules;
    TextView rating_system, rating_system_in_rules, less_than_250, greater_than_1500, rating_scheme_in_rules;
    TextView x_points, a_points, b_points, c_points, step_1, bap, step_2, result, the_last_one;
    public View decorView;
    Button back_to_main, rus, arm, eng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_rules);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            if (visibility == 0)
                decorView.setSystemUiVisibility(HideTheBars.hideSystemBars());
        });

        buttonClickSound = MediaPlayer.create(this, R.raw.button_click_sound_1);
        // Set the volume of the mediaPlayer to a lower level (background music volume)
        buttonClickSound.setVolume(BACKGROUND_MUSIC_VOLUME, BACKGROUND_MUSIC_VOLUME);

        intro = findViewById(R.id.intro);
        intro_in_rules = findViewById(R.id.intro_in_rules);
        quick_game_in_rules = findViewById(R.id.quick_game_in_rules);
        case_1 = findViewById(R.id.case_1);
        case_2 = findViewById(R.id.case_2);
        points_in_rules = findViewById(R.id.points_in_rules);
        coins_in_rules = findViewById(R.id.coins_in_rules);
        rating_system = findViewById(R.id.rating_system);
        rating_system_in_rules = findViewById(R.id.rating_system_in_rules);
        less_than_250 = findViewById(R.id.less_than_250);
        greater_than_1500 = findViewById(R.id.greater_than_1500);
        rating_scheme_in_rules = findViewById(R.id.rating_scheme_in_rules);
        x_points = findViewById(R.id.x_points);
        a_points = findViewById(R.id.a_points);
        b_points = findViewById(R.id.b_points);
        c_points = findViewById(R.id.c_points);
        step_1 = findViewById(R.id.step_1);
        bap = findViewById(R.id.bap);
        step_2 = findViewById(R.id.step_2);
        result = findViewById(R.id.result);
        the_last_one = findViewById(R.id.the_last_one);

        eng = findViewById(R.id.eng);
        rus = findViewById(R.id.rus);
        arm = findViewById(R.id.arm);
        back_to_main = findViewById(R.id.back_to_main);

        eng.setOnClickListener(task -> {
            buttonClickSound.start();
            rus.setBackgroundColor(getResources().getColor(R.color.grey_1));
            rus.setTextColor(getResources().getColor(R.color.grey));
            eng.setBackgroundColor(getResources().getColor(R.color.grey));
            eng.setTextColor(getResources().getColor(R.color.grey_1));
            arm.setBackgroundColor(getResources().getColor(R.color.grey_1));
            arm.setTextColor(getResources().getColor(R.color.grey));
            intro.setText("Introduction");
            intro_in_rules.setText("In this game the number of cards is 32, and the number of players is 4. It should be mentioned that one of the players shall be the user (you), whereas the other three shall be bots. At the beginning of the game eight cards shall be handed to each player at a time. At that moment only the first of Quick Game, Classic Game and Big Game modes is active.");
            quick_game_in_rules.setText("This game consists of 4 rounds, and the game shall be started by the user (not the first step of each round, but it is you who always puts the first playing card). For winning it is necessary to score the maximum points. Here the trump first of all depends on the first playing card the user has put.  For example:");
            case_1.setText("In this case it is the first player who wins");
            case_2.setText("In this case it is the third player who wins");
            points_in_rules.setText("In this turn a winner is given 10 points, but if one of 4 playing cards is the king of hearts, 70 points shall be deducted from the winner’s points. It should also be mentioned, that at each play round the sum of points gained by all players equals to zero.");
            coins_in_rules.setText("So that the user is able to play one of the game modes, which in this case is the only acessible Quick Game mode, he should  donate 100 out of his initial 2000 gold coins. In case of winning the user shall gain 300 gold coins, and in case of losing shall gain nothing.");
            rating_system.setText("The Rating System And Titles");
            rating_system_in_rules.setText("All the users shall have an initial rating – 200 and titles which shall change depending on rating (a rating may also be negative).");
            less_than_250.setText("less than 250 - Newbie");
            greater_than_1500.setText("greater than or equal to - KING");
            rating_scheme_in_rules.setText("The increase or decrease of a rating shall be established by the following scheme: let us suppose that upon 4 game rounds the final results will be:");
            x_points.setText("User - x points");
            a_points.setText("First bot - a points");
            b_points.setText("SEcond bot - b points");
            c_points.setText("Third bot – c points");
            step_1.setText("Step 1. The average of points gained by 3 bots shall be calculated as:");
            bap.setText("(a + b + c) / 3 = BAP (Bots Average Point)");
            step_2.setText("Step 2: The average of the average points gained by a user and bots shall be calculated as:");
            result.setText("(x + BAP) / 2 = R (Result)");
            the_last_one.setText("The obtained R number shall be added to the current rating of a user (R may also be as positive, as well as negative, as well as 0).");
        });

        arm.setOnClickListener(v -> {
            buttonClickSound.start();
            rus.setBackgroundColor(getResources().getColor(R.color.grey_1));
            rus.setTextColor(getResources().getColor(R.color.grey));
            eng.setBackgroundColor(getResources().getColor(R.color.grey_1));
            eng.setTextColor(getResources().getColor(R.color.grey));
            arm.setBackgroundColor(getResources().getColor(R.color.grey));
            arm.setTextColor(getResources().getColor(R.color.grey_1));
            intro.setText("Ներածություն");
            intro_in_rules.setText("Այս խաղում խաղաքարտերի քանակը 32 է, իսկ խաղացողների քանակը՝ 4։ Պետք է նշել, որ խաղացողներից մեկը օգտատերն է (դու), իսկ մյուս երեքը՝ ռոբոտներ (բոտեր)։ Խաղի սկզբում բոլոր խաղացողներին տրվում է ութական խաղաքարտ։ Տվյալ պահին Quick Game, Classic Game և Big Game ռեժիմներից ակտիվ է միայն առաջինը։");
            quick_game_in_rules.setText("Այստեղ խաղը բաղկացած է 4 խաղափուլից, և խաղը միշտ սկսելու է օգտատերը (ոչ թե ամեն խաղափուլի առաջին քայլը, այլ առաջին խաղաքարտը միշտ գցում ես դու)։ Հաղթելու համար անհրաժեշտ է հավաքել ամենաշատ միավորները։  Այդտեղ հաղթաթուղթը (կոզիրը) ամեն հերթին կախված է օգտատիրոջ գցած առաջին խաղաքարտից։ Օրինակ՝");
            case_1.setText("Հաղթում է առաջին խաղացողը");
            case_2.setText("Հաղթում է երրորդ խաղացողը");
            points_in_rules.setText("Տվյալ հերթին հաղթողին տրվում է 10 միավոր, բայց եթե այդ հերթի 4 խաղաքարերից մեկը սրտի թագավորն է, հաղթողից հանվում է 70 միավոր։ Պետք է նաև նշել, որ ամեն խաղափուլում բոլոր խաղացողների հավաքած միավորների գումարը 0 է։ ");
            coins_in_rules.setText("Որպեսզի օգտատերը կարողանա խաղալ ռեժիմներից մեկը, որը տվյալ դեպքում միակ հասանելի՝ Quick Game ռեժիմն է, նա պետք է իր սկզբնական 2000 ոսկիներից 100-ը զոհաբերի․ հաղթանակի դեպքում օգտատերը շահում է 300 ոսկի, իսկ պարտության դեպքում ՝ ոչինչ չի շահում։");
            rating_system.setText("Վարկանշային Համակարգը և Կոչումները");
            rating_system_in_rules.setText("Բոլոր օգտատերերը ունեն սկզբնական վարկանիշ ՝ 200 և կոչումներ, որոնք փոխվում են ՝ կախված վարկանշից (վարկանիշը կարող է լինել նաև բացասական)․");
            less_than_250.setText("խիստ փոքր 250-ից - Newbie");
            greater_than_1500.setText("1500-ից մեծ կամ հավասար - KING");
            rating_scheme_in_rules.setText("Վարկանշի աճը կամ անկումը որոշվում է հետևյալ սխեմայով․ ենթադրենք ՝ 4 խաղափուլերի ավարտից հետո վերջնական արդյունքներն են․");
            x_points.setText("Օգտատեր – x միավոր");
            a_points.setText("Առաջին բոտ – a միավոր");
            b_points.setText("Երկրորդ բոտ – b միավոր");
            c_points.setText("Երրորդ բոտ – c միավոր");
            step_1.setText("Քայլ 1։ Հաշվվում է 3 բոտերի հավաքած միավորների միջինը․");
            bap.setText("(a + b + c) / 3 = BAP (Bots Average Point)");
            step_2.setText("Քայլ 2։ Հաշվվում է օգտատիրոջ և բոտերի հավաքած միավորների միջինի միավորների միջինը․");
            result.setText("(x + BAP) / 2 = R (Result)");
            the_last_one.setText("Ստացված R թիվը գումարվում է օգտատիրոջ ընթացիկ վարկանիշին (R-ը կարող է լինել և դրական, և բացասական, և 0)։");
        });

        rus.setOnClickListener(task -> {
            buttonClickSound.start();
            rus.setBackgroundColor(getResources().getColor(R.color.grey));
            rus.setTextColor(getResources().getColor(R.color.grey_1));
            eng.setBackgroundColor(getResources().getColor(R.color.grey_1));
            eng.setTextColor(getResources().getColor(R.color.grey));
            arm.setBackgroundColor(getResources().getColor(R.color.grey_1));
            arm.setTextColor(getResources().getColor(R.color.grey));
            intro.setText("Введение");
            intro_in_rules.setText("В данной игре количество игральных карт 32, а количество игроков – 4. Необходимо отметить, что одним из игроков является пользователь (ты), а остальные три игрока роботами (ботами). В начале игры всем игрокам раздается по восемь карт. В данный момент только один из режимов Quick Game, Classic Game и Big Game активен.");
            quick_game_in_rules.setText("Здесь игра состоит из 4 раундов, и игру всегда начинает пользователь (не первый шаг каждого раунда) и всегда ты первым ставишь карту. Для выигрыша необходимо набрать максимальное количество очков. Здесь козырь в каждой очереди зависит от поставленной пользователем первой карты. К примеру:");
            case_1.setText("Здесь выигрывает первый игрок");
            case_2.setText("Здесь выигрывает третий игрок");
            points_in_rules.setText("В данной очереди победитель получает 10 очков, но если один из 4 игральных карт данной очереди червовый король, то у победителя вычитается 70 очков. Необходимо также отметить, что набранная всеми игроками во всех раундах сумма очков равна нулю.");
            coins_in_rules.setText("Для того чтобы пользователь смог сыграть один из режимов, который в данном случае единственно доступный Quick Game режим, он должен пожертвовать 100 из своих начальных 2000 золотых фишек; в случае выигрыша пользователь получает 300 золотых фишек, а в случае проигрыша пользователь ничего не выигрывает.");
            rating_system.setText("Рейтинговая Система и Звания");
            rating_system_in_rules.setText("У всех пользователей имеется начальный рейтинг – 200 и звания, которые меняются в зависимости от рейтинга (рейтинг может быть также отрицательным).");
            less_than_250.setText("меньше чем 250 - Newbie");
            greater_than_1500.setText("больше чем или ровно 1500 - KING");
            rating_scheme_in_rules.setText("Рост или падение рейтинга определяется по следующей схеме: предположим к концу 4 раундов имеются следуюшие результаты:");
            x_points.setText("Пользователь– x очков");
            a_points.setText("Первый бот – a очков");
            b_points.setText("Второй бот – b очков");
            c_points.setText("Третий бот – c очков");
            step_1.setText("Шаг 1։ Подсчитывается среднее набранных 3 ботами очков:");
            bap.setText("(a + b + c) / 3 = BAP (Bots Average Point)");
            step_2.setText("Шаг 2։ Подсчитывается среднее очков среднего набранных пользователем и ботами очков:");
            result.setText("(x + BAP) / 2 = R (Result)");
            the_last_one.setText("Полученное R число прибавляется к текущему рейтингу пользователя (R может быть положительным, отрицательным и равным нулю).");
        });

        back_to_main.setOnClickListener(task -> {
            buttonClickSound.start();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            decorView.setSystemUiVisibility(HideTheBars.hideSystemBars());
    }
}