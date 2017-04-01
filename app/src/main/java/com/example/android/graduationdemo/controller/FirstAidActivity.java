package com.example.android.graduationdemo.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.android.graduationdemo.adapter.FirstAidCustomAdapter;
import com.example.android.graduationdemo.R;
import com.example.android.graduationdemo.data.FirstAid;
import com.example.android.graduationdemo.data.FirstAidList;

import java.util.ArrayList;

public class FirstAidActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_aid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("First Aid");

        recyclerView = (RecyclerView) findViewById(R.id.firstaidrecyclerview);

        ArrayList<FirstAid> firstAidArr = addFirstAid();

        ArrayList<FirstAidList> arrayList = new ArrayList<>();
        FirstAidList obj = new FirstAidList();
        obj.setName("Allergies");
        obj.setImage(R.drawable.allergiesicon);
        arrayList.add(obj);
        obj = new FirstAidList();
        obj.setName("Asthma Attack");
        obj.setImage(R.drawable.asthmaicon);
        arrayList.add(obj);
        obj = new FirstAidList();
        obj.setName("Bleeding");
        obj.setImage(R.drawable.bleedingicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Broken Bones");
        obj.setImage(R.drawable.boneicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Burns");
        obj.setImage(R.drawable.burnsicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Choking");
        obj.setImage(R.drawable.chokingicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Chest Pain");
        obj.setImage(R.drawable.chestpainicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Diabets");
        obj.setImage(R.drawable.diabetesicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Heading");
        obj.setImage(R.drawable.headingicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Heat Stroke");
        obj.setImage(R.drawable.heatstrokeicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Muscle Injury");
        obj.setImage(R.drawable.muscleinjuryicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Nose Bleeding");
        obj.setImage(R.drawable.nosebleedicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Poisoning");
        obj.setImage(R.drawable.poisoningicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Stroke");
        obj.setImage(R.drawable.strokeicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Unconscious");
        obj.setImage(R.drawable.unconsciousicon);
        arrayList.add(obj);
        FirstAidCustomAdapter adapter = new FirstAidCustomAdapter(arrayList,firstAidArr,getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<FirstAid> addFirstAid() {

        ArrayList<FirstAid> firstAidArr = new ArrayList<>();
        FirstAid firstAidObj = new FirstAid();
        firstAidObj.setSign("Cough difficulty or irregular breathing, wheezing, itchy throat or mouth, and difficulty swallowing\n" +
                "Nausea, vomiting, abdominal pain, and diarrhea\n" +
                "Itchiness, red bumps or welts on the skin (hives), and skin redness\n" +
                "Dizziness, lightheadedness, heart palpitations, chest discomfort or tightness, mental confusion, weakness, lower blood pressure, rapid pulse, loss of consciousness, and fainting\n");
        firstAidObj.setCure("If you have severe allergies, you should keep two epinephrine injection kits with you at all times and readily available. \n" +
                "If you experience any sign of anaphylaxis, do not hesitate to use the auto-injector even if those symptoms do not appear to be allergy related.\n" +
                "Using the auto injector as a precaution will not harm you.\n");
        firstAidObj.setImage(R.drawable.allergies);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("You have increasing wheezing, cough, chest tightness or shortness of breath  \n" +
                "You are waking often at night with asthma symptoms \n");
        firstAidObj.setCure("Sit the person upright comfortably and loosen tight clothing.\n" +
                "If the person has asthma medication, such as an inhaler, assist in using it.\n" +
                "If the person doesn’t have an inhaler, use one from a first aid kit or borrow someone else’s.\n");
        firstAidObj.setImage(R.drawable.asthma);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Pain in the Abdomen\n" +
                "Blood in the stool\n" +
                "Vomiting blood\n" +
                "Coughing blood\n" +
                "Bloody Urine\n" +
                "Vaginal bleeding\n" +
                "Compartment Syndrome\n" +
                "Shock\n" +
                "Painful joints\n" +
                "Bleeding in the head\n");
        firstAidObj.setCure("Remove any obvious dirt or debris from the wound. Don't remove large or deeply embedded objects.\n" +
                " Don't probe the wound or attempt to clean it yet. Your first job is to stop the bleeding. \n" +
                "Wear disposable protective gloves if available.\n" +
                "Stop the bleeding. Place a sterile bandage or clean cloth on the wound. \n" +
                "Press the bandage firmly with your palm to control bleeding. \n" +
                "Maintain pressure by binding the wound tightly with a bandage or a piece of clean cloth.\n" +
                " Secure with adhesive tape. Use your hands if nothing else is available.\n");
        firstAidObj.setImage(R.drawable.bleeding);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Broken bone can cause one or more of the following signs and symptoms:\n" +
                "Intense pain in the injured area that gets worse when you move it\n" +
                "Numbness in the injured area\n" +
                "Bluish color, swelling, or visible deformity in the injured area\n" +
                "Bone protruding through the skin\n" +
                "Heavy bleeding at the injury site\n");
        firstAidObj.setCure("Stop any bleeding: If they’re bleeding, elevate and apply pressure to the wound using a sterile bandage, a clean cloth, or a clean piece of clothing.\n" +
                "Immobilize the injured area: If you suspect they’ve broken a bone in their neck or back, help them stay as still as possible. \n" +
                "If you suspect they’ve broken a bone in one of their limbs, immobilize the area using a splint or sling.\n" +
                "Apply cold to the area: Wrap an ice pack or bag of ice cubes in a piece of cloth and apply it to the injured area for up to 10 minutes at a time.\n" +
                "Treat them for shock: Help them get into a comfortable position, encourage them to rest, and reassure them.\n" +
                " Cover them with a blanket or clothing to keep them warm.\n");
        firstAidObj.setImage(R.drawable.brokenbone2);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("");
        firstAidObj.setCure("First, stop the burning to prevent a more severe burn.\n" +
                "Cover the burn with a clean, dry cloth to reduce the risk of infection.\n" +
                "Do not put any salve or medicine on the burned area, so your doctor can properly assess your burn.\n" +
                "Do not put ice or butter on the burned area, because these measures do not help and can damage the skin tissue.\n");
        firstAidObj.setImage(R.drawable.burns);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Inability to talk\n" +
                "Difficulty breathing or noisy breathing\n" +
                "Inability to cough forcefully\n" +
                "Skin, lips and nails turning blue or dusky\n" +
                "Loss of consciousness\n");
        firstAidObj.setCure("If choking is occurring, the Red Cross recommends a \"five-and-five\" approach to delivering first aid:\n" +
                "Give 5 back blows. First, deliver five back blows between the person's shoulder blades with the heel of your hand.\n" +
                "Give 5 abdominal thrusts. Perform five abdominal thrusts (also known as the Heimlich maneuver).\n" +
                "Alternate between 5 blows and 5 thrusts until the blockage is dislodged\n");
        firstAidObj.setImage(R.drawable.choking);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Chest Discomfort\n" +
                "Nausea, Indigestion, Heartburn, or Stomach Pain\n");
        firstAidObj.setCure("If the person gets angina and has been prescribed nitroglycerin:\n" +
                "Dissolve 1 nitroglycerin tablet under the tongue (or use nitroglycerin spray under the tongue).\n" +
                "If the person has been diagnosed with chronic stable angina:\n" +
                "Dissolve 1 nitroglycerin tablet under the tongue (or use nitroglycerin spray under the tongue).\n" +
                "Repeat every 5 minutes until the person has taken 3 tablets in 15 minutes.\n");
        firstAidObj.setImage(R.drawable.chestpain1);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Increased thirst\n" +
                "Increased hunger (especially after eating)\n" +
                "Dry mouth\n" +
                "Frequent urination or urine infections\n" +
                "Unexplained weight loss (even though you are eating and feel hungry)\n" +
                "Fatigue (weak, tired feeling)\n" +
                "Blurred vision\n" +
                "Headaches\n");

        firstAidObj.setCure("*What you need to do ‒ for high blood sugar (hyperglycemia)\n" +
                "Checking their breathing, pulse and level of response.\n" +
                "If they lose responsiveness at any point, open their airway,\n" +
                "Check their breathing and prepare to treat someone who’s become unresponsive.\n" +
                "*What you need to do ‒ for low blood sugar (hypoglycemia)\n" +
                "Help them sit down. If they have their own glucose gel, help them take it.\n" +
                " If not, you need to give them something sugary like fruit juice, a fizzy drink, three teaspoons of sugar, or sugary sweets.\n" +
                "If they improve quickly, give them more sugary food or drink and let them rest. If they have their glucose testing kit with them, \n" +
                "Help them use it to check their glucose level. Stay with them until they feel completely better.\n" +
                "While waiting, keep checking their responsiveness, breathing and pulse.\n");

        firstAidObj.setImage(R.drawable.diabetes1);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Becomes very sleepy\n" +
                "Behaves abnormally\n" +
                "Develops a severe headache or stiff neck\n" +
                "Has a pupil (the dark central part of the eye) of unequal sizes\n" +
                "Is unable to move an arm or leg\n" +
                "Loses consciousness, even briefly\n" +
                "Vomits more than once\n");

        firstAidObj.setCure("Check the person's airway, breathing, and circulation. If necessary, begin rescue breathing and CPR.\n" +
                "If the person's breathing and heart rate are normal but the person is unconscious,\n" +
                "treat as if there is a spinal injury. Stabilize the head and neck by placing your hands on both sides of the person's head.\n" +
                " Keep the head in line with the spine and prevent movement. Wait for medical help.\n" +
                "Stop any bleeding by firmly pressing a clean cloth on the wound. If the injury is serious, be careful not to move the person's head. If blood soaks through the cloth, do not remove it. Place another cloth over the first one.\n");
        firstAidObj.setImage(R.drawable.heading);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Throbbing headache\n" +
                "Dizziness and light-headedness\n" +
                "Lack of sweating despite the heat\n" +
                "Red, hot, and dry skin\n" +
                "Muscle weakness or cramps\n" +
                "Nausea and vomiting\n" +
                "Rapid heartbeat, which may be either strong or weak\n" +
                "Rapid, shallow breathing\n" +
                "Behavioral changes such as confusion, disorientation, or staggering\n" +
                "Seizures\n" +
                "Unconsciousness\n");
        firstAidObj.setCure("Immediately move the person out of the heat and cool him or her by whatever means available, for example:\n" +
                "Put the person in a cool tub of water or a cool shower.\n" +
                "Spray with a garden hose.\n" +
                "Sponge with cool water.\n" +
                "Fan while misting with cool water.\n" +
                "Place ice packs or cool wet towels on the neck, armpits and groin.\n" +
                "Cover with cool damp sheets.\n");
        firstAidObj.setImage(R.drawable.heatstroke);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Swelling, bruising, or redness due to the injury\n" +
                "Pain at rest\n" +
                "Pain when the specific muscle or the joint in relation to that muscle is used\n" +
                "Weakness of the muscle or tendons\n" +
                "Inability to use the muscle at all\n");
        firstAidObj.setCure(" Control Swelling With RICE Therapy\n" +
                "RICE stands for:\n" +
                "-Rest the sprained or strained areaIf necessary, use a sling for an arm injury or crutches for a leg or foot injury. \n" +
                "Splint an injured finger or toe by taping it to an adjacent finger or toe.\n" +
                "-Ice for 20 minutes every hour. Never put ice directly against the skin or it may damage the skin. \n" +
                "Use a thin towel for protection.\n" +
                "-Compress by wrapping an elastic (Ace) bandage or sleeve lightly (not tightly) around the joint or limb. \n" +
                "Specialized braces, such as for the ankle, can work better than an elastic bandage for removing the swelling.\n" +
                "-Elevate the area above heart level if possible.\n" +
                "Let the person drink cool water or other nonalcoholic beverage without caffeine\n");
        firstAidObj.setImage(R.drawable.muscleinjury);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Tiredness\n" +
                "Weakness\n" +
                "Chills or feeling cold\n" +
                "Dizziness or feeling lightheaded\n" +
                "Easy bruising or bleeding\n");
        firstAidObj.setCure("Have the person sit up straight and lean forward slightly. \n" +
                "Don't have the person lie down or tilt the head backward.\n" +
                "With thumb and index finger, firmly pinch the nose just below the bone up against the face.\n" +
                "Apply pressure for 5 minutes. Time yourself with a clock.\n" +
                "If bleeding continues after 5 minutes, repeat the process.\n");
        firstAidObj.setImage(R.drawable.nosebleeding);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Poisoning signs and symptoms can mimic other conditions, such as seizure, alcohol intoxication, \n" +
                "Stroke and insulin reaction. Signs and symptoms of poisoning may include:\n" +
                "Burns or redness around the mouth and lips\n" +
                "Breathe that smells like chemicals, such as gasoline or paint thinner\n" +
                "Vomiting, Difficulty breathing, Drowsiness, Confusion or other altered mental status\n");
        firstAidObj.setCure("Wallowed poison. Remove anything remaining in the person's mouth. \n" +
                "If the suspected poison is a household cleaner or other chemical, read the container's label and follow instructions for accidental poisoning.\n" +
                "Poison on the skin. Remove any contaminated clothing using gloves. Rinse the skin for 15 to 20 minutes in a shower or with a hose.\n" +
                "Poison in the eye. Gently flush the eye with cool or lukewarm water for 20 minutes or until help arrives.\n");
        firstAidObj.setImage(R.drawable.poisoning);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Weakness or numbness on one side of the body, including either leg\n" +
                "Dimness, blurring or loss of vision, particularly in one eye\n" +
                "Severe headache — a bolt out of the blue — with no apparent cause\n" +
                "Unexplained dizziness, unsteadiness or a sudden fall, especially if accompanied by any of the other signs or symptoms\n");
        firstAidObj.setCure(" Assess the patient’s level of consciousness\n" +
                "1. If unconscious and breathing normally, or if not fully alert, place the patient on their side in a supported position.\n" +
                "It is important for the patient to be assessed as soon as possible because treatment must be started within 1 to 2 hours if a clot is present in the brain.\n" +
                "2.      Care for a conscious patient\n" +
                "Assist a conscious patient into the position of greatest comfort\n" +
                "Cover the patient to reduce heat loss.\n" +
                "3.      Observe the patient \n" +
                "While waiting for the ambulance to arrive, observe the patient closely for any change in condition.\n" +
                "If there is any deterioration in the patient’s conscious state, turn the patient on their side in a supported position.\n");
        firstAidObj.setImage(R.drawable.stroke);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Symptoms that may indicate that unconsciousness is about to occur include:\n" +
                "Sudden inability to respond\n" +
                "Slurred speech\n" +
                "A rapid heartbeat\n" +
                "Confusion\n" +
                "Dizziness or lightheadedness\n");
        firstAidObj.setCure("Check whether the person is breathing.\n" +
                "Raise the person’s legs at least 12 inches above the ground.\n" +
                "Loosen any restrictive clothing or belts. If the person doesn’t regain consciousness within one minute, \n" +
                "Check the person’s airway to make sure there’s no obstruction.\n" +
                "Check again to see if the person is breathing, coughing, or moving. These are signs of positive circulation. \n" +
                "If these signs are absent, perform cardiopulmonary resuscitation (CPR) until emergency personnel arrive.\n");
        firstAidObj.setImage(R.drawable.unconsciousness);

        firstAidArr.add(firstAidObj);

        return firstAidArr;
    }

}

