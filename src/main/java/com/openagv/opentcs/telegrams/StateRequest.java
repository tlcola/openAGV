package com.openagv.opentcs.telegrams;

import com.openagv.core.AppContext;
import com.openagv.opentcs.model.ProcessModel;
import com.openagv.tools.SettingUtils;
import com.openagv.tools.ToolsKit;
import org.opentcs.data.order.Route;
import org.opentcs.drivers.vehicle.MovementCommand;

/**
 *  状态请求对象，该对象由OpenTCS在移动车辆时产生，属于openTCS主动发起的请求
 *
 * @author Laotang
 */
public class StateRequest extends AbsRequest {

    /**当前/起始点**/
    private String currentPointName;
    /**下一个点*/
    private String nextPointName;
    /**最终目的点，即停车点*/
    private String endPointName;
    /** 车辆名称*/
    private String vehicleName;

    private StateRequest() {
        super(TelegramType.STATE);
    }

    private StateRequest(String vehicleName, String currentPointName, String nextPointName, String endPointName) {
        super(TelegramType.STATE);
        this.vehicleName = vehicleName;
        this.currentPointName = currentPointName;
        this.nextPointName = nextPointName;
        this.endPointName = endPointName;
    }

    public static class Builder {

        /**车辆参数模型*/
        private ProcessModel processModel;
        /**车辆移动命令*/
        private MovementCommand movementCommand;
        private Route.Step step;

        public Builder model(ProcessModel processModel) {
            this.processModel = processModel;
            return this;
        }

        public Builder command(MovementCommand movementCommand) {
            this.movementCommand = movementCommand;
            step = movementCommand.getStep();
            return this;
        }

        public StateRequest build() {
            StateRequest stateRequest = new StateRequest(
                    processModel.getVehicleReference().getName(),
                    step.getSourcePoint().getName(),
                    step.getDestinationPoint().getName(),
                    movementCommand.getFinalDestination().getName()
            );
            stateRequest.setTarget(AppContext.getStateRequestTarget());
            return stateRequest;
        }
    }

    @Override
    public void setTarget(String target) {
        super.target = target;
    }

    public String getCurrentPointName() {
        return currentPointName;
    }

    public String getNextPointName() {
        return nextPointName;
    }

    public String getEndPointName() {
        return endPointName;
    }

    public String getVehicleName() {
        return vehicleName;
    }
}
