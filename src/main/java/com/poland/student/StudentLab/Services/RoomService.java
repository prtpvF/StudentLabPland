package com.poland.student.StudentLab.Services;

import com.poland.student.StudentLab.Exception.RoomNotFoundException;
import com.poland.student.StudentLab.Model.Image;
import com.poland.student.StudentLab.Model.Room;
import com.poland.student.StudentLab.Repo.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepo roomRepo;

    public void saveRoom(Room room, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            room.addImage(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            room.addImage(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            room.addImage(image3);
        }
        Room roomFromDb = roomRepo.save(room);
        roomFromDb.setPreviewImageId(roomFromDb.getImages().get(0).getId());
        roomRepo.save(room);
    }

    private static Image toImageEntity(MultipartFile file1) throws IOException { //конвертирование фотографии в сущность
        Image image = new Image();
        image.setName(file1.getName());
        image.setOriginalFilename(file1.getOriginalFilename());
        image.setContentType(file1.getContentType());
        image.setSize(file1.getSize());
        image.setBytes(file1.getBytes());
        return image;
    }

    public void deleteRoom(int id){
        roomRepo.deleteById(id);
    }

    public Room findRoom(int id) throws RoomNotFoundException {
        Optional<Room> room = roomRepo.findById(id);
        if (room.isEmpty()){
            throw new RoomNotFoundException("room is nor found");
        }
        return room.get();
    }

    public void update(int id, Room updatedRoom) throws RoomNotFoundException {
        Optional<Room> roomToBeUpdated = roomRepo.findById(id);
        if (roomToBeUpdated.isEmpty()){
            throw new RoomNotFoundException("room is not found");
        }
        updatedRoom.setId(roomToBeUpdated.get().getId());
        updatedRoom.setBookings(roomToBeUpdated.get().getBookings());
    }

    public List<Room> findAll(){
        return roomRepo.findAll();
    }
}
