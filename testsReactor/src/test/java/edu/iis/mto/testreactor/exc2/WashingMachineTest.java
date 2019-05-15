package edu.iis.mto.testreactor.exc2;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WashingMachineTest {

    @Mock
    private DirtDetector dirtDetector;

    @Mock
    Engine engine;

    @Mock
    WaterPump waterPump;

    WashingMachine washingMachine;

    LaundryBatch laundryBatch;
    ProgramConfiguration programConfiguration;

    @Before public void initialize() {
        setDefaultLaundryBatch();
        programConfiguration=ProgramConfiguration.builder().withProgram(Program.MEDIUM).withSpin(true).build();
    }

    @Test
    public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

    @Test
    public void givenBatchWith10Kg_whenWashingMachineStart_thenLaundryStatusIsFailureBecauseOfWeight() {

        //given
        setLaundryBatchWithWeight(10);

        //when
       washingMachine=new WashingMachine(dirtDetector,engine,waterPump);
       LaundryStatus laundryStatus=washingMachine.start(laundryBatch,programConfiguration);

       //then

        Assert.assertThat(laundryStatus.getResult(),is(Result.FAILURE));
        Assert.assertThat(laundryStatus.getErrorCode(),is(ErrorCode.TOO_HEAVY));

    }



    private void setDefaultLaundryBatch() {
        laundryBatch= LaundryBatch.builder().withType(Material.COTTON).withWeightKg(5D).build();
    }

    private void setLaundryBatchWithWeight(double weight) {
        laundryBatch= LaundryBatch.builder().withType(Material.COTTON).withWeightKg(weight).build();
    }
}
