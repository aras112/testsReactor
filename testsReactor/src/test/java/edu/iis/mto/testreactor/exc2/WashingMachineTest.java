package edu.iis.mto.testreactor.exc2;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        setDefaultConfiguration();
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
        LaundryStatus laundryStatus = startWashing();

        //then

        Assert.assertThat(laundryStatus.getResult(),is(Result.FAILURE));
        Assert.assertThat(laundryStatus.getErrorCode(),is(ErrorCode.TOO_HEAVY));

    }

    @Test
    public void givenDefaultBatch_whenWashingMachineStart_thenLaundryStatusIsSuccess() {

        //given
        setDefaultLaundryBatch();

        //when
        LaundryStatus laundryStatus = startWashing();

        //then
        Assert.assertThat(laundryStatus.getResult(),is(Result.SUCCESS));
    }
    @Test
    public void givenDefaultBatchAndMediumProgram_whenWashingMachineStart_thenLaundryStatusIsSuccessAndProgramIsMedium() {

        //given
        setDefaultLaundryBatch();
        setConfigurationWithProgram(Program.MEDIUM);

        //when
        LaundryStatus laundryStatus = startWashing();

        //then
        Assert.assertThat(laundryStatus.getResult(),is(Result.SUCCESS));
        Assert.assertThat(laundryStatus.getRunnedProgram(),is(Program.MEDIUM));
    }
    @Test
    public void givenAutodetectProgramAnd41pDirtLevel_whenWashingMachineStart_thenLaundryStatusIsSuccessAndProgramIsLong() {

        //given
        setConfigurationWithProgram(Program.AUTODETECT);
        when(dirtDetector.detectDirtDegree(any())).thenReturn(new Percentage(41));

        //when
        LaundryStatus laundryStatus = startWashing();

        //then
        Assert.assertThat(laundryStatus.getResult(),is(Result.SUCCESS));
        Assert.assertThat(laundryStatus.getRunnedProgram(),is(Program.LONG));
    }
    @Test
    public void givenAutodetectProgramAnd40pDirtLevel_whenWashingMachineStart_thenLaundryStatusIsSuccessAndProgramIsMedium() {

        //given
        setConfigurationWithProgram(Program.AUTODETECT);
        when(dirtDetector.detectDirtDegree(any())).thenReturn(new Percentage(40));

        //when
        LaundryStatus laundryStatus = startWashing();

        //then
        Assert.assertThat(laundryStatus.getResult(),is(Result.SUCCESS));
        Assert.assertThat(laundryStatus.getRunnedProgram(),is(Program.MEDIUM));
    }
    @Test
    public void givenAutodetectProgram_whenWashingMachineStart_thenDirtDetectorRunsDetectFunction() {

        //given
        setConfigurationWithProgram(Program.AUTODETECT);
        when(dirtDetector.detectDirtDegree(any())).thenReturn(new Percentage(40));

        //when
        LaundryStatus laundryStatus = startWashing();

        //then
        verify(dirtDetector, Mockito.times(1)).detectDirtDegree(any());
    }
    @Test
    public void givenLongProgram_whenWashingMachineStart_thenDirtDetectorDoesNotRunDetectFunction() {

        //given
        setConfigurationWithProgram(Program.LONG);
        when(dirtDetector.detectDirtDegree(any())).thenReturn(new Percentage(40));

        //when
        LaundryStatus laundryStatus = startWashing();

        //then
        verify(dirtDetector, Mockito.times(0)).detectDirtDegree(any());
    }

    private LaundryStatus startWashing() {
        washingMachine = new WashingMachine(dirtDetector, engine, waterPump);
        return washingMachine.start(laundryBatch, programConfiguration);
    }

    private void setDefaultLaundryBatch() {
        laundryBatch= LaundryBatch.builder().withType(Material.COTTON).withWeightKg(5D).build();
    }

    private void setLaundryBatchWithWeight(double weight) {
        laundryBatch= LaundryBatch.builder().withType(Material.COTTON).withWeightKg(weight).build();
    }

    private void setDefaultConfiguration() {
        programConfiguration= ProgramConfiguration.builder().withProgram(Program.MEDIUM).withSpin(true).build();
    }private void setConfigurationWithProgram(Program program) {
        programConfiguration= ProgramConfiguration.builder().withProgram(program).withSpin(true).build();
    }
}
